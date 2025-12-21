const API = "/api/v1";

let pieChart;
let barChart;
let lineChart;

/* ================= AUTH HEADERS ================= */
function authHeaders() {
    return {
        "Content-Type": "application/json",
        "Authorization": "Bearer " + localStorage.getItem("jwtToken")
    };
}

/* ================= ROUTING & EVENTS ================= */
document.addEventListener("DOMContentLoaded", () => {

    const path = window.location.pathname;
    const token = localStorage.getItem("jwtToken");

    /* ---------- ROUTING ---------- */

    // Root → Login
    if (path === "/" && !token) {
        window.location.replace("/login.html");
        return;
    }

    // Login / Register → Dashboard (if already logged in)
    if ((path.endsWith("login.html") || path.endsWith("register.html")) && token) {
        window.location.replace("/index.html");
        return;
    }

    // Dashboard → Login (if not logged in)
    if (path.endsWith("index.html") && !token) {
        window.location.replace("/login.html");
        return;
    }

    /* ---------- LOGIN ---------- */
    const loginBtn = document.getElementById("btnLogin");
    if (loginBtn) {
        loginBtn.addEventListener("click", async () => {

            const username = document.getElementById("loginUser").value.trim();
            const password = document.getElementById("loginPass").value.trim();
            const msg = document.getElementById("loginMsg");

            if (!username || !password) {
                msg.innerText = "Enter username & password";
                return;
            }

            try {
                const res = await fetch(`${API}/auth/login`, {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({ username, password })
                });

                if (!res.ok) {
                    msg.innerText = "Invalid credentials";
                    return;
                }

                const data = await res.json();
                localStorage.setItem("jwtToken", data.token);
                localStorage.setItem("username", username);

                window.location.replace("/index.html");

            } catch (err) {
                msg.innerText = "Server error";
            }
        });
    }

    /* ---------- REGISTER ---------- */
    const registerBtn = document.getElementById("btnRegister");
    if (registerBtn) {
        registerBtn.addEventListener("click", async () => {

            const username = document.getElementById("regUser").value.trim();
            const password = document.getElementById("regPass").value.trim();
            const msg = document.getElementById("regMsg");

            if (!username || !password) {
                msg.innerText = "Fill all fields";
                return;
            }

            try {
                const res = await fetch(`${API}/auth/register`, {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({ username, password })
                });

                if (res.ok) {
                    msg.innerText = "Registered successfully ✅";
                    setTimeout(() => window.location.replace("/login.html"), 1000);
                } else {
                    msg.innerText = "Registration failed ❌";
                }
            } catch (err) {
                msg.innerText = "Server error ❌";
            }
        });
    }

    /* ---------- DASHBOARD LOAD ---------- */
    if (path.endsWith("index.html") && token) {

        const welcome = document.getElementById("welcomeUser");
        if (welcome) {
            welcome.innerText = "Welcome, " + localStorage.getItem("username");
        }

        loadCategories();
        loadExpenses();
    }

    /* ---------- LOGOUT ---------- */
    const logoutBtn = document.getElementById("btnLogout");
    if (logoutBtn) {
        logoutBtn.addEventListener("click", () => {
            localStorage.clear();
            window.location.replace("/login.html");
        });
    }

    /* ---------- BUTTONS ---------- */
    document.getElementById("btnAddCategory")?.addEventListener("click", addCategory);
    document.getElementById("btnAddExpense")?.addEventListener("click", saveExpense);
});

/* ================= CATEGORY ================= */
async function loadCategories() {
    const res = await fetch(`${API}/category`, { headers: authHeaders() });
    if (!res.ok) return;

    const data = await res.json();

    const select = document.getElementById("categorySelect");
    select.innerHTML = "";

    data.forEach(c => {
        select.innerHTML += `<option value="${c.id}">${c.name}</option>`;
    });

    // Dashboard summary
    document.getElementById("totalCategories").innerText = data.length;
}

async function addCategory() {
    await fetch(`${API}/category`, {
        method: "POST",
        headers: authHeaders(),
        body: JSON.stringify({
            name: categoryName.value,
            description: categoryDesc.value
        })
    });

    categoryName.value = "";
    categoryDesc.value = "";
    loadCategories();
}

/* ================= EXPENSE ================= */
async function loadExpenses() {
    const res = await fetch(`${API}/expenses`, { headers: authHeaders() });
    if (!res.ok) return;

    const expenses = await res.json();
    const list = document.getElementById("expenseList");
    list.innerHTML = "";

    const totals = {};
    let totalAmount = 0;

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
                        onclick='editExpense(${JSON.stringify(e)})'>Edit</button>
                    <button class="btn btn-danger btn-sm"
                        onclick="deleteExpense(${e.expenseId})">Delete</button>
                </td>
            </tr>
        `;

        totals[e.categoryId] = (totals[e.categoryId] || 0) + e.amount;
        totalAmount += e.amount;
    });

    // Dashboard summary
    document.getElementById("totalEntries").innerText = expenses.length;
    document.getElementById("totalExpense").innerText = "₹" + totalAmount;

    drawCharts(expenses);
}

async function saveExpense() {
    const id = expenseId.value;

    const payload = {
        name: expenseName.value,
        amount: expenseAmount.value,
        categoryId: categorySelect.value,
        creationDate: expenseDate.value,
        comments: expenseComments.value
    };

    await fetch(id ? `${API}/expenses/${id}` : `${API}/expenses`, {
        method: id ? "PUT" : "POST",
        headers: authHeaders(),
        body: JSON.stringify(payload)
    });

    expenseId.value = "";
    expenseName.value = "";
    expenseAmount.value = "";
    expenseDate.value = "";
    expenseComments.value = "";

    loadExpenses();
}

function editExpense(e) {
    expenseId.value = e.expenseId;
    expenseName.value = e.name;
    expenseAmount.value = e.amount;
    expenseDate.value = e.creationDate;
    expenseComments.value = e.comments || "";
    categorySelect.value = e.categoryId;
}

async function deleteExpense(id) {
    if (!confirm("Delete expense?")) return;

    await fetch(`${API}/expenses/${id}`, {
        method: "DELETE",
        headers: authHeaders()
    });

    loadExpenses();
}

function drawCharts(expenses) {

    const categoryTotals = {};
    const monthlyTotals = {};

    expenses.forEach(e => {
        // CATEGORY TOTALS
        categoryTotals[e.categoryId] =
            (categoryTotals[e.categoryId] || 0) + Number(e.amount);

        // MONTHLY TOTALS
        const month = e.creationDate.slice(0, 7); // YYYY-MM
        monthlyTotals[month] =
            (monthlyTotals[month] || 0) + Number(e.amount);
    });

    const categoryLabels = Object.keys(categoryTotals);
    const categoryValues = Object.values(categoryTotals);

    const monthLabels = Object.keys(monthlyTotals);
    const monthValues = Object.values(monthlyTotals);

    /* ---------- PIE CHART ---------- */
    if (pieChart) pieChart.destroy();
    pieChart = new Chart(document.getElementById("expenseChart"), {
        type: "pie",
        data: {
            labels: categoryLabels,
            datasets: [{
                data: categoryValues,
                backgroundColor: [
                    "#ff6384", "#36a2eb", "#ffce56", "#4bc0c0", "#9966ff"
                ]
            }]
        }
    });

    /* ---------- BAR CHART ---------- */
    if (barChart) barChart.destroy();
    barChart = new Chart(document.getElementById("barChart"), {
        type: "bar",
        data: {
            labels: categoryLabels,
            datasets: [{
                label: "Expenses",
                data: categoryValues,
                backgroundColor: "#36a2eb"
            }]
        }
    });

    /* ---------- LINE CHART ---------- */
    if (lineChart) lineChart.destroy();
    lineChart = new Chart(document.getElementById("lineChart"), {
        type: "line",
        data: {
            labels: monthLabels,
            datasets: [{
                label: "Monthly Expense",
                data: monthValues,
                borderColor: "#ff6384",
                fill: false
            }]
        }
    });
}