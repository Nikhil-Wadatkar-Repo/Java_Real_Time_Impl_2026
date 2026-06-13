import React, { useEffect, useState } from "react";
import APIServices from "./APIServices";
import "./EmployeesPage.css";

function EmployeesPage() {
  const [employees, setEmployees] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [currentPage, setCurrentPage] = useState(0);
  const [pageSize, setPageSize] = useState(50);
  const [totalEmployees, setTotalEmployees] = useState(0);
  const [exporting, setExporting] = useState(false);

  useEffect(() => {
    const fetchEmployees = async () => {
      try {
        setLoading(true);
        const data = await APIServices.getEmployees(currentPage, pageSize);
        setEmployees(Array.isArray(data) ? data : []);
        // Assuming backend returns an array; adjust if it returns { content, totalElements }
        setTotalEmployees(Array.isArray(data) ? data.length : 0);
      } catch (err) {
        setError(err.message || "Unable to load employees.");
      } finally {
        setLoading(false);
      }
    };

    fetchEmployees();
  }, [currentPage, pageSize]);

  const handlePageSizeChange = (e) => {
    setPageSize(Number(e.target.value));
    setCurrentPage(0);
  };

  const handleNextPage = () => {
    setCurrentPage(currentPage + 1);
  };

  const handlePreviousPage = () => {
    if (currentPage > 0) {
      setCurrentPage(currentPage - 1);
    }
  };

  const handleExtractPdf = async () => {
    try {
      setExporting(true);
      await APIServices.extractEmployeesPdf();
    } catch (err) {
      setError(err.message || "Failed to export PDF.");
    } finally {
      setExporting(false);
    }
  };

  return (
    <div className="EmployeesPage">
      <div className="EmployeesHeader">
        <h1>Employees</h1>
        <button
          className="ExtractPdfBtn"
          onClick={handleExtractPdf}
          disabled={exporting || loading}
        >
          {exporting ? "Exporting..." : "Extract PDF"}
        </button>
      </div>

      {loading && <p>Loading employees...</p>}
      {error && <p className="error">{error}</p>}

      {!loading && !error && (
        <div>
          <div className="ControlsSection">
            <div className="RowsPerPageControl">
              <label htmlFor="pageSize">Show:</label>
              <select
                id="pageSize"
                value={pageSize}
                onChange={handlePageSizeChange}
              >
                <option value={10}>10</option>
                <option value={25}>25</option>
                <option value={50}>50</option>
                <option value={100}>100</option>
              </select>
              <span>rows per page</span>
            </div>
          </div>

          {employees.length === 0 ? (
            <p>No employees found.</p>
          ) : (
            <>
              <table className="EmployeesTable">
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Email</th>
                    <th>Position</th>
                  </tr>
                </thead>
                <tbody>
                  {employees.map((employee) => (
                    <tr key={employee.id || employee.email || Math.random()}>
                      <td>{employee.id || "-"}</td>
                      <td>{employee.name || employee.fullName || "-"}</td>
                      <td>{employee.email || "-"}</td>
                      <td>{employee.position || employee.role || "-"}</td>
                    </tr>
                  ))}
                </tbody>
              </table>

              <div className="PaginationControls">
                <button
                  onClick={handlePreviousPage}
                  disabled={currentPage === 0}
                  className="PaginationBtn"
                >
                  Previous
                </button>
                <span className="PageInfo">Page {currentPage + 1}</span>
                <button
                  onClick={handleNextPage}
                  disabled={employees.length < pageSize}
                  className="PaginationBtn"
                >
                  Next
                </button>
              </div>
            </>
          )}
        </div>
      )}
    </div>
  );
}

export default EmployeesPage;
