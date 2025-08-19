import { useEffect, useState } from "react";
import api from "../api/axios";
import TransactionModal from "../components/TransactionModal";

export default function TransactionsPage() {
  const [transactions, setTransactions] = useState([]);
  const [filteredTransactions, setFilteredTransactions] = useState([]);
  const [sortConfig, setSortConfig] = useState({
    key: "date",
    direction: "desc",
  });
  const [currentPage, setCurrentPage] = useState(1);
  const itemsPerPage = 5;

  // Filters
  const [typeFilter, setTypeFilter] = useState("");
  const [categoryFilter, setCategoryFilter] = useState("");
  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");
  const [searchTitle, setSearchTitle] = useState("");

  const [modalOpen, setModalOpen] = useState(false);
  const [editingTransaction, setEditingTransaction] = useState(null);

  // Fetch data
  useEffect(() => {
    api.get("/transactions").then((res) => setTransactions(res.data));
  }, []);

  // Apply filters and search
  useEffect(() => {
    let temp = [...transactions];

    if (typeFilter) temp = temp.filter((t) => t.type === typeFilter);
    if (categoryFilter) temp = temp.filter((t) => t.category === categoryFilter);
    if (startDate) temp = temp.filter((t) => new Date(t.date) >= new Date(startDate));
    if (endDate) temp = temp.filter((t) => new Date(t.date) <= new Date(endDate));
    if (searchTitle)
      temp = temp.filter((t) =>
        t.title.toLowerCase().includes(searchTitle.toLowerCase())
      );

    setFilteredTransactions(temp);
    setCurrentPage(1);
  }, [transactions, typeFilter, categoryFilter, startDate, endDate, searchTitle]);

  // Sorting filtered transactions
  const sortedTransactions = [...filteredTransactions].sort((a, b) => {
    const { key, direction } = sortConfig;

    if (key === "amount") {
      return direction === "asc" ? a.amount - b.amount : b.amount - a.amount;
    } else if (key === "date") {
      return direction === "asc"
        ? new Date(a.date) - new Date(b.date)
        : new Date(b.date) - new Date(a.date);
    } else {
      return direction === "asc"
        ? a[key].localeCompare(b[key])
        : b[key].localeCompare(a[key]);
    }
  });

  // Pagination
  const indexOfLast = currentPage * itemsPerPage;
  const indexOfFirst = indexOfLast - itemsPerPage;
  const currentTransactions = sortedTransactions.slice(indexOfFirst, indexOfLast);
  const totalPages = Math.ceil(filteredTransactions.length / itemsPerPage);

  const handleSort = (key) => {
    setSortConfig((prev) => ({
      key,
      direction: prev.key === key && prev.direction === "asc" ? "desc" : "asc",
    }));
  };

  const getSortArrow = (key) => {
    if (sortConfig.key !== key) return "";
    return sortConfig.direction === "asc" ? "↑" : "↓";
  };

  const openModal = (transaction = null) => {
    setEditingTransaction(transaction);
    setModalOpen(true);
  };

  const handleSaveTransaction = async (form) => {
    try {
      if (editingTransaction) {
        // Edit transaction
        const res = await api.put(`/transactions/${editingTransaction.id}`, form);
        setTransactions((prev) =>
          prev.map((t) => (t.id === editingTransaction.id ? res.data : t))
        );
      } else {
        // Add new transaction
        const res = await api.post("/transactions", form);
        setTransactions((prev) => [res.data, ...prev]);
      }
      setModalOpen(false);
      setEditingTransaction(null);
    } catch (error) {
      console.error(error);
    }
  };

  const handleEdit = (transaction) => {
    setEditingTransaction(transaction);
    setModalOpen(true);
  };

  const handleDelete = async (id) => {
    if (window.confirm("Are you sure you want to delete this transaction?")) {
      try {
        await api.delete(`/transactions/${id}`);
        setTransactions((prev) => prev.filter((t) => t.id !== id));
      } catch (error) {
        console.error(error);
      }
    }
  };

  return (
    <div className="p-6 space-y-6">
      <h2 className="text-2xl font-bold">All Transactions</h2>

      {/* Filters */}
      <div className="flex flex-wrap gap-4 mb-4">
        <select
          value={typeFilter}
          onChange={(e) => setTypeFilter(e.target.value)}
          className="border p-2 rounded"
        >
          <option value="">All Types</option>
          <option value="INCOME">Income</option>
          <option value="EXPENSE">Expense</option>
        </select>

        <select
          value={categoryFilter}
          onChange={(e) => setCategoryFilter(e.target.value)}
          className="border p-2 rounded"
        >
          <option value="">All Categories</option>
          <option value="FOOD">Food</option>
          <option value="GROCERIES">Groceries</option>
          <option value="RENT">Rent</option>
          <option value="UTILITIES">Utilities</option>
          <option value="TRAVEL">Travel</option>
          <option value="HEALTH">Health</option>
          <option value="ENTERTAINMENT">Entertainment</option>
          <option value="EDUCATION">Education</option>
          <option value="SUBSCRIPTIONS">Subscriptions</option>
          <option value="OTHER_EXPENSE">Other Expense</option>
          <option value="SALARY">Salary</option>
          <option value="FREELANCE">Freelance</option>
          <option value="BUSINESS">Business</option>
          <option value="INVESTMENT">Investment</option>
          <option value="GIFTS">Gifts</option>
          <option value="OTHER_INCOME">Other Income</option>
        </select>

        <input
          type="date"
          value={startDate}
          onChange={(e) => setStartDate(e.target.value)}
          className="border p-2 rounded"
        />
        <input
          type="date"
          value={endDate}
          onChange={(e) => setEndDate(e.target.value)}
          className="border p-2 rounded"
        />

        <input
          type="text"
          placeholder="Search by title..."
          value={searchTitle}
          onChange={(e) => setSearchTitle(e.target.value)}
          className="border p-2 rounded flex-grow"
        />
      </div>

      <div className="bg-white p-4 rounded-xl shadow">
        <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center mb-2 gap-2">
          <h2 className="text-lg font-semibold">Transactions</h2>
          <button
            onClick={() => openModal()}
            className="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700 transition"
          >
            + Add Transaction
          </button>
        </div>

        <div className="overflow-x-auto">
          <table className="min-w-full border text-sm sm:text-base">
            <thead>
              <tr className="bg-gray-100 text-left">
                <th className="p-2 cursor-pointer" onClick={() => handleSort("date")}>
                  Date {getSortArrow("date")}
                </th>
                <th className="p-2 cursor-pointer" onClick={() => handleSort("title")}>
                  Title {getSortArrow("title")}
                </th>
                <th className="p-2 cursor-pointer" onClick={() => handleSort("category")}>
                  Category {getSortArrow("category")}
                </th>
                <th className="p-2 cursor-pointer" onClick={() => handleSort("type")}>
                  Type {getSortArrow("type")}
                </th>
                <th className="p-2 cursor-pointer" onClick={() => handleSort("amount")}>
                  Amount {getSortArrow("amount")}
                </th>
                <th className="p-2">Actions</th>
              </tr>
            </thead>
            <tbody>
              {currentTransactions.length > 0 ? (
                currentTransactions.map((t) => (
                  <tr key={t.id} className="border-t hover:bg-gray-50">
                    <td className="p-2 whitespace-nowrap">
                      {new Date(t.date).toLocaleDateString()}
                    </td>
                    <td className="p-2">{t.title}</td>
                    <td className="p-2">{t.category}</td>
                    <td className="p-2">{t.type}</td>
                    <td className="p-2 whitespace-nowrap">₹{t.amount}</td>
                    <td className="p-2 flex gap-2">
                      <button
                        onClick={() => handleEdit(t)}
                        className="px-2 py-1 bg-yellow-400 text-white rounded hover:bg-yellow-500"
                      >
                        Edit
                      </button>
                      <button
                        onClick={() => handleDelete(t.id)}
                        className="px-2 py-1 bg-red-500 text-white rounded hover:bg-red-600"
                      >
                        Delete
                      </button>
                    </td>
                  </tr>
                ))
              ) : (
                <tr key="no-transactions">
                  <td className="p-2 text-center" colSpan={6}>
                    No transactions found.
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>

      {/* Pagination */}
      <div className="flex justify-between mt-4">
        <button
          onClick={() => setCurrentPage((p) => Math.max(p - 1, 1))}
          disabled={currentPage === 1}
          className="px-4 py-2 bg-gray-200 rounded disabled:opacity-50"
        >
          Prev
        </button>
        <span>
          Page {currentPage} of {totalPages}
        </span>
        <button
          onClick={() => setCurrentPage((p) => Math.min(p + 1, totalPages))}
          disabled={currentPage === totalPages || totalPages === 0}
          className="px-4 py-2 bg-gray-200 rounded disabled:opacity-50"
        >
          Next
        </button>
      </div>

      <TransactionModal
        isOpen={modalOpen}
        onClose={() => {
          setModalOpen(false);
          setEditingTransaction(null);
        }}
        onSave={handleSaveTransaction}
        transaction={editingTransaction}
      />
    </div>
  );
}
