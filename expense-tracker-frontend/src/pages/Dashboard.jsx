import { useEffect, useState } from "react";
import api from "../api/axios";
import TransactionModal from "../components/TransactionModal";
import {
  PieChart,
  Pie,
  Cell,
  Tooltip,
  Legend,
  ResponsiveContainer,
} from "recharts";

export default function Dashboard() {
  const [summary, setSummary] = useState({
    income: 0,
    expense: 0,
    netBalance: 0,
  });
  const [transactions, setTransactions] = useState([]);
  const [categoryTotals, setCategoryTotals] = useState({});
  const [showModal, setShowModal] = useState(false);
  const [addingTransaction, setAddingTransaction] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const fetchData = async () => {
    setLoading(true);
    setError(null);
    try {
      const [summaryRes, categoryRes, transactionsRes] = await Promise.all([
        api.get("/report/summary"),
        api.get("/report/by-category"),
        api.get("/transactions"),
      ]);
      setSummary(summaryRes.data);
      setCategoryTotals(categoryRes.data);
      setTransactions(transactionsRes.data);
    } catch (err) {
      setError("Registration failed. Try again.");
      console.error("Failed to fetch dashboard data:", err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  const recentTransactions = [...transactions]
    .sort((a, b) => new Date(b.date) - new Date(a.date))
    .slice(0, 5);

  const openModal = (transaction = null) => {
    setAddingTransaction(transaction);
    setShowModal(true);
  };

  const handleSaveTransaction = async (form) => {
    try {
      await api.post("/transactions", form);
      setShowModal(false);
      setAddingTransaction(null);
      fetchData();
    } catch (error) {
      console.error(error);
    }
  };

  const CATEGORY_COLORS = {
    FOOD: "#0088FE",
    GROCERIES: "#00C49F",
    RENT: "#FFBB28",
    UTILITIES: "#FF8042",
    TRAVEL: "#FF4560",
    HEALTH: "#775DD0",
    ENTERTAINMENT: "#3F51B5",
    EDUCATION: "#2ECC71",
    SUBSCRIPTIONS: "#F39C12",
    OTHER_EXPENSE: "#8E44AD",
    SALARY: "#1ABC9C",
    FREELANCE: "#E91E63",
    BUSINESS: "#3498DB",
    INVESTMENT: "#9C27B0",
    GIFTS: "#F44336",
    OTHER_INCOME: "#607D8B",
  };

  const entries = Object.entries(categoryTotals).sort((a, b) => b[1] - a[1]);
  const hasCategoryData = entries.reduce((sum, [_, val]) => sum + val, 0) > 0;

  const pieData = entries.map(([cat, total]) => ({
    name: cat,
    value: total,
  }));

  return (
    <>
      {loading ? (
        <div className="flex justify-center items-center h-64">
          <span className="text-gray-500">Loading dashboard...</span>
        </div>
      ) : error ? (
        <div className="text-red-500 text-center py-4">{error}</div>
      ) : (
        <div className="p-6 space-y-6 max-w-full w-full">
          {/* Summary cards */}
          <div className="grid grid-cols-1 sm:grid-cols-3 gap-4 w-full">
            <div className="bg-green-100 p-4 rounded-xl shadow w-full">
              <h2 className="text-lg font-semibold">Income</h2>
              <p className="text-xl font-bold text-green-700">
                ₹{summary.income}
              </p>
            </div>
            <div className="bg-red-100 p-4 rounded-xl shadow w-full">
              <h2 className="text-lg font-semibold">Expense</h2>
              <p className="text-xl font-bold text-red-700">
                ₹{summary.expense}
              </p>
            </div>
            <div className="bg-blue-100 p-4 rounded-xl shadow w-full">
              <h2 className="text-lg font-semibold">Net Balance</h2>
              <p className="text-xl font-bold text-blue-700">
                ₹{summary.netBalance}
              </p>
            </div>
          </div>

          {/* Category totals */}
          <div className="bg-white p-4 rounded-xl shadow w-full overflow-x-auto">
            <h2 className="text-lg font-semibold mb-2">Category Totals</h2>
            {hasCategoryData ? (
              <ul className="min-w-[300px]">
                {Object.entries(categoryTotals)
                  .sort((a, b) => b[1] - a[1])
                  .map(([cat, total]) => (
                    <li
                      key={cat}
                      className="flex justify-between border-b py-1 items-center"
                    >
                      <div className="flex items-center gap-2">
                        <span
                          className="w-3 h-3 rounded-full"
                          style={{
                            backgroundColor: CATEGORY_COLORS[cat] || "#CCC",
                          }}
                        />
                        <span>{cat}</span>
                      </div>
                      <span>
                        ₹
                        {total.toLocaleString("en-IN", {
                          minimumFractionDigits: 2,
                        })}
                      </span>
                    </li>
                  ))}
              </ul>
            ) : (
              <div className="text-gray-500 text-center py-4">
                No category data available yet.
              </div>
            )}
          </div>

          {/* Spending by Category Pie Chart */}
          <div className="bg-white pb-15 rounded-xl shadow w-full max-w-full">
            <h2 className="text-lg sm:text-xl font-semibold mb-4 text-center sm:text-left">
              Spending by Category
            </h2>

            {pieData.length > 0 ? (
              <div
                className="w-full max-w-4xl mx-auto"
                style={{ height: "300px", maxWidth: "100%" }}
              >
                <ResponsiveContainer width="100%" height="100%">
                  <PieChart>
                    <Pie
                      data={pieData}
                      dataKey="value"
                      nameKey="name"
                      outerRadius={80}
                      fill="#8884d8"
                      label
                    >
                      {pieData.map(({ name }, index) => (
                        <Cell
                          key={`cell-${index}`}
                          fill={CATEGORY_COLORS[name] || "#CCCCCC"}
                        />
                      ))}
                    </Pie>
                    <Tooltip />
                    <Legend verticalAlign="bottom" height={36} />
                  </PieChart>
                </ResponsiveContainer>
              </div>
            ) : (
              <div className="flex items-center justify-center h-48 text-gray-500">
                <ResponsiveContainer width="100%" height="100%">
                  <PieChart>
                    <Pie
                      data={[{ name: "Nothing to show", value: 1 }]}
                      dataKey="value"
                      nameKey="name"
                      outerRadius={80}
                      fill="#8884d8"
                      label
                    >
                      <Cell fill="#CCCCCC" />
                    </Pie>
                    <Tooltip />
                    <Legend verticalAlign="bottom" height={36} />
                  </PieChart>
                </ResponsiveContainer>
              </div>
            )}
          </div>

          {/* Recent transactions */}
          <div className="bg-white p-4 rounded-xl shadow w-full overflow-x-auto">
            <div className="flex justify-between items-center mb-2 flex-wrap gap-2">
              <h2 className="text-lg font-semibold">Recent Transactions</h2>
              <button
                onClick={() => openModal()}
                className="bg-blue-600 text-white px-4 py-2 rounded-md whitespace-nowrap"
              >
                + Add Transaction
              </button>
            </div>

            <table className="w-full border min-w-[600px]">
              <thead>
                <tr className="bg-gray-100 text-left">
                  <th className="p-2">Date</th>
                  <th className="p-2">Title</th>
                  <th className="p-2">Category</th>
                  <th className="p-2">Type</th>
                  <th className="p-2">Amount</th>
                </tr>
              </thead>
              <tbody>
                {recentTransactions.length > 0 ? (
                  recentTransactions.map((t) => (
                    <tr key={t.id} className="border-t">
                      <td className="p-2 whitespace-nowrap">
                        {new Date(t.date).toLocaleDateString()}
                      </td>
                      <td className="p-2">{t.title}</td>
                      <td className="p-2">{t.category}</td>
                      <td className="p-2">{t.type}</td>
                      <td className="p-2">₹{t.amount}</td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td className="p-2 text-center" colSpan={5}>
                      No transactions found.
                    </td>
                  </tr>
                )}
              </tbody>
            </table>

            <TransactionModal
              isOpen={showModal}
              onClose={() => {
                setShowModal(false);
                setAddingTransaction(null);
              }}
              onSave={handleSaveTransaction}
              transaction={addingTransaction}
            />
          </div>
        </div>
      )}
    </>
  );
}
