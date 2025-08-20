import { useState, useEffect } from "react";

export default function TransactionModal({
  isOpen,
  onClose,
  onSave,
  transaction,
}) {
  const EXPENSE_CATEGORIES = [
    "Food",
    "Groceries",
    "Rent",
    "Utilities",
    "Travel",
    "Health",
    "Entertainment",
    "Education",
    "Subscriptions",
    "Other Expense",
  ];
  const INCOME_CATEGORIES = [
    "Salary",
    "Freelance",
    "Business",
    "Investment",
    "Gifts",
    "Other Income",
  ];

  const categoryMap = {
    Food: "FOOD",
    Groceries: "GROCERIES",
    Rent: "RENT",
    Utilities: "UTILITIES",
    Travel: "TRAVEL",
    Health: "HEALTH",
    Entertainment: "ENTERTAINMENT",
    Education: "EDUCATION",
    Subscriptions: "SUBSCRIPTIONS",
    "Other Expense": "OTHER_EXPENSE",

    Salary: "SALARY",
    Freelance: "FREELANCE",
    Business: "BUSINESS",
    Investment: "INVESTMENT",
    Gifts: "GIFTS",
    "Other Income": "OTHER_INCOME",
  };

  const [form, setForm] = useState({
    title: "",
    amount: "",
    type: "INCOME",
    category: "Salary",
    date: "",
  });

  const [saving, setSaving] = useState(false);

  // Initialize or reset form when modal opens or transaction changes
  useEffect(() => {
    if (isOpen) {
      if (transaction) {
        const displayCategory =
          Object.entries(categoryMap).find(
            ([display, key]) => key === transaction.category
          )?.[0] || transaction.category;

        const formattedDate = transaction.date
          ? new Date(transaction.date).toISOString().split("T")[0]
          : "";

        setForm({
          title: transaction.title,
          amount: transaction.amount,
          type: transaction.type,
          category: displayCategory,
          date: formattedDate,
        });
      } else {
        setForm({
          title: "",
          amount: "",
          type: "INCOME",
          category: "Salary",
          date: "",
        });
      }
    } else {
      // Reset form on modal close
      setForm({
        title: "",
        amount: "",
        type: "INCOME",
        category: "Salary",
        date: "",
      });
    }
  }, [isOpen, transaction]);

  const handleChange = (e) => {
    const { name, value } = e.target;

    setForm((prev) => {
      if (name === "type") {
        return {
          ...prev,
          type: value,
          category: value === "EXPENSE" ? "Food" : "Salary",
        };
      }
      return { ...prev, [name]: value };
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    setSaving(true);

    try {
      if (!form.title.trim()) {
        alert("Title is required");
        return;
      }

      if (Number(form.amount) <= 0) {
        alert("Amount must be greater than zero");
        return;
      }

      const payload = {
        ...form,
        amount: Number(form.amount),
        category: categoryMap[form.category],
      };

      onSave(payload);
    } catch (error) {
      console.log(error);
    } finally {
      setSaving(false);
    }
  };

  if (!isOpen) return null;

  const categories =
    form.type === "EXPENSE" ? EXPENSE_CATEGORIES : INCOME_CATEGORIES;

  return (
    <div className="fixed inset-0 bg-transparent backdrop-blur-sm backdrop-brightness-75 bg-opacity-40 flex justify-center items-center z-50 p-4 sm:p-6 overflow-auto">
      <div className="bg-white p-6 rounded shadow-md w-full max-w-md sm:max-w-lg md:max-w-xl mx-auto">
        <h2 className="text-xl font-bold mb-4 text-center sm:text-left">
          {transaction ? "Edit" : "Add"} Transaction
        </h2>

        <form onSubmit={handleSubmit} className="space-y-3">
          <label htmlFor="title" className="sr-only">
            Title
          </label>
          <input
            id="title"
            type="text"
            name="title"
            value={form.title}
            onChange={handleChange}
            placeholder="Title"
            required
            className="w-full border p-2 rounded"
          />

          <label htmlFor="amount" className="sr-only">
            Amount
          </label>
          <input
            id="amount"
            type="number"
            name="amount"
            value={form.amount}
            onChange={handleChange}
            placeholder="Amount"
            min="0.01"
            step="0.01"
            required
            className="w-full border p-2 rounded"
          />

          <label htmlFor="type" className="sr-only">
            Type
          </label>
          <select
            id="type"
            name="type"
            value={form.type}
            onChange={handleChange}
            className="w-full border p-2 rounded"
          >
            <option value="INCOME">Income</option>
            <option value="EXPENSE">Expense</option>
          </select>

          <label htmlFor="category" className="sr-only">
            Category
          </label>
          <select
            id="category"
            name="category"
            value={form.category}
            onChange={handleChange}
            className="w-full border p-2 rounded"
          >
            {categories.map((cat) => (
              <option key={cat} value={cat}>
                {cat}
              </option>
            ))}
          </select>

          <label htmlFor="date" className="sr-only">
            Date
          </label>
          <input
            id="date"
            type="date"
            name="date"
            value={form.date}
            onChange={handleChange}
            required
            className="w-full border p-2 rounded"
          />

          <div className="flex flex-col sm:flex-row justify-end gap-2 mt-3">
            <button
              type="button"
              onClick={onClose}
              className="px-3 py-1 border rounded w-full sm:w-auto"
            >
              Cancel
            </button>
            <button
              type="submit"
              className="px-3 py-1 bg-blue-600 text-white rounded w-full sm:w-auto"
            >
              {transaction ? "Update" : "Add"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
