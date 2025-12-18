const API = "/api/v1";
let chart;

/* ================= AUTH HEADERS ================= */
function authHeaders() {
    return {
        "Content-Type": "application/json",
        "Authorization": "Bearer " + localStorage.getItem("jwtToken")
    };
}



/* ================= LOGIN ================= */
document.addEventListener("DOMContentLoaded", () => {

    const loginBtn = document.getElementById("btnLogin");
    if (!loginBtn) return;

    loginBtn.onclick = async () => {
        const username = document.getElementById("loginUser").value.trim();
        const password = document.getElementById("loginPass").value.trim();
        const msg = document.getElementById("loginMsg");

        if (!username || !password) {
            msg.textContent = "Username and password required";
            msg.style.color = "red";
            return;
        }

        try {
            const res = await fetch("/api/v1/auth/login", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ username, password })
            });

            if (!res.ok) {
                msg.textContent = "Invalid credentials";
                msg.style.color = "red";
                return;
            }

            const data = await res.json();

            // SAVE TOKEN
            localStorage.setItem("jwtToken", data.token);
            localStorage.setItem("username", username);

            // REDIRECT
            window.location.href = "/index.html";

        } catch (err) {
            msg.textContent = "Server error";
            msg.style.color = "red";
        }
    };
});




/* ================= PAGE LOAD ================= */
document.addEventListener("DOMContentLoaded", () => {

    // Protect index page
    if (location.pathname.includes("index.html")) {
        const token = localStorage.getItem("jwtToken");

        if (!token) {
            window.location.href = "/login.html";
            return;
        }

        const user = localStorage.getItem("username");
        const welcome = document.getElementById("welcomeUser");
        if (welcome) welcome.textContent = "Welcome, " + user;

        loadCategories();
        loadExpenses();
    }

    // Logout
    const logoutBtn = document.getElementById("btnLogout");
    if (logoutBtn) {
        logoutBtn.onclick = () => {
            localStorage.clear();
            window.location.href = "/login.html";
        };
    }

    // Add category
    const addCategoryBtn = document.getElementById("btnAddCategory");
    if (addCategoryBtn) {
        addCategoryBtn.onclick = addCategory;
    }

    // Add / update expense
    const addExpenseBtn = document.getElementById("btnAddExpense");
    if (addExpenseBtn) {
        addExpenseBtn.onclick = saveExpense;
    }
});

/* ================= CATEGORY ================= */
async function loadCategories() {
    const res = await fetch(API + "/category", {
        headers: authHeaders()
    });

    if (!res.ok) return;

    const data = await res.json();
    const select = document.getElementById("categorySelect");
    select.innerHTML = "";

    data.forEach(c => {
        select.innerHTML += `<option value="${c.id}">${c.name}</option>`;
    });
}

async function addCategory() {
    const name = categoryName.value.trim();
    const description = categoryDesc.value.trim();

    if (!name) {
        alert("Category name required");
        return;
    }

    await fetch(API + "/category", {
        method: "POST",
        headers: authHeaders(),
        body: JSON.stringify({ name, description })
    });

    categoryName.value = "";
    categoryDesc.value = "";
    loadCategories();
}

/* ================= EXPENSE ================= */
async function loadExpenses() {
    const res = await fetch(API + "/expenses", {
        headers: authHeaders()
    });

    if (!res.ok) return;

    const expenses = await res.json();
    const list = document.getElementById("expenseList");
    list.innerHTML = "";

    const totals = {};

    expenses.forEach(e => {
        list.innerHTML += `
            <tr>
                <td>${e.name}</td>
                <td>${e.amount}</td>
                <td>${e.creationDate}</td>
                <td>${e.categoryId}</td>
                <td>${e.comments || ""}</td>
                <td>
                    <button class="btn btn-warning btn-sm"
                        onclick='editExpense(${JSON.stringify(e)})'>
                        Edit
                    </button>
                    <button class="btn btn-danger btn-sm"
                        onclick="deleteExpense(${e.expenseId})">
                        Delete
                    </button>
                </td>
            </tr>
        `;

        totals[e.categoryId] = (totals[e.categoryId] || 0) + e.amount;
    });

    drawChart(totals);
}

async function saveExpense() {
    const payload = {
        name: expenseName.value.trim(),
        amount: expenseAmount.value,
        categoryId: categorySelect.value,
        creationDate: expenseDate.value,
        comments: expenseComments.value.trim()
    };

    if (!payload.name || !payload.amount) {
        alert("Expense name and amount required");
        return;
    }

    const id = expenseId.value;
    const method = id ? "PUT" : "POST";
    const url = id
        ? `${API}/expenses/${id}`
        : `${API}/expenses`;

    await fetch(url, {
        method,
        headers: authHeaders(),
        body: JSON.stringify(payload)
    });

    expenseId.value = "";
    expenseName.value = "";
    expenseAmount.value = "";
    expenseComments.value = "";
    expenseDate.value = "";

    loadExpenses();
}

/* ================= EDIT ================= */
function editExpense(e) {
    expenseId.value = e.expenseId;
    expenseName.value = e.name;
    expenseAmount.value = e.amount;
    expenseDate.value = e.creationDate;
    expenseComments.value = e.comments || "";
    categorySelect.value = e.categoryId;
}

/* ================= DELETE ================= */
async function deleteExpense(id) {
    if (!confirm("Delete expense?")) return;

    await fetch(`${API}/expenses/${id}`, {
        method: "DELETE",
        headers: authHeaders()
    });

    loadExpenses();
}

/* ================= CHART ================= */
function drawChart(data) {
    const canvas = document.getElementById("expenseChart");
    if (!canvas) return;

    if (chart) chart.destroy();

    chart = new Chart(canvas, {
        type: "pie",
        data: {
            labels: Object.keys(data),
            datasets: [{
                data: Object.values(data),
                backgroundColor: [
                    "#ff6384",
                    "#36a2eb",
                    "#ffce56",
                    "#4bc0c0",
                    "#9966ff"
                ]
            }]
        }
    });
}