import {
  BrowserRouter as Router,
  Routes,
  Route,
  Navigate,
  useNavigate,
  Link,
} from "react-router";
import Login from "../src/pages/Login";
import Register from "../src/pages/Register";
import Dashboard from "../src/pages/Dashboard";
import TransactionsPage from "../src/pages/TransactionsPage";
import PrivateRoute from "../src/components/PrivateRoute";

function Navbar() {
  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.removeItem("token"); // clear auth token
    navigate("/login"); // redirect to login
  };

  return (
    <nav className="bg-gray-800 text-white p-4 flex justify-between items-center">
      <div className="flex gap-6 items-center">
        {/* App Logo/Title */}
        <Link
          to="/dashboard"
          className="text-2xl font-bold text-white hover:text-blue-400"
        >
          Trackeroo
        </Link>

        <div className="flex gap-4">
          <Link to="/dashboard" className="hover:text-blue-400">
            Dashboard
          </Link>
          <Link to="/transactions" className="hover:text-blue-400">
            Transactions
          </Link>
        </div>
      </div>
      <button
        onClick={handleLogout}
        className="bg-red-500 hover:bg-red-600 px-3 py-1 rounded"
      >
        Logout
      </button>
    </nav>
  );
}

function Header() {
  return (
    <header className="bg-gray-800 text-white p-4 text-center">
      <h1 className="text-2xl font-bold">Trackeroo</h1>
      <p className="text-gray-300 text-sm">An Expense Tracker</p>
    </header>
  );
}

function NotFound() {
  return (
    <div className="p-6 text-center">
      <h1 className="text-4xl font-bold mb-4">404 - Page Not Found</h1>
      <p>The page you’re looking for does not exist.</p>
      <Link
        to="/dashboard"
        className="text-blue-600 underline mt-4 inline-block"
      >
        Go to Dashboard
      </Link>
    </div>
  );
}

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Navigate to="/dashboard" replace />} />

        <Route
          path="/login"
          element={
            <>
              <Header /> <Login />
            </>
          }
        />
        <Route
          path="/register"
          element={
            <>
              <Header />
              <Register />
            </>
          }
        />

        <Route
          path="/dashboard"
          element={
            <PrivateRoute>
              <>
                <Navbar />
                <Dashboard />
              </>
            </PrivateRoute>
          }
        />

        <Route
          path="/transactions"
          element={
            <PrivateRoute>
              <>
                <Navbar />
                <TransactionsPage />
              </>
            </PrivateRoute>
          }
        />

        {/* Catch-all 404 page */}
        <Route path="*" element={<NotFound />} />
      </Routes>
    </Router>
  );
}

export default App;
