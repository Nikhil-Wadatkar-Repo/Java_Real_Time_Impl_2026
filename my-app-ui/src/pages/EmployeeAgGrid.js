import React, { useEffect, useMemo, useRef, useState } from "react";
import APIServices from "./APIServices";
import { AllCommunityModule } from "ag-grid-community";
import { AgGridProvider, AgGridReact } from "ag-grid-react";
// import { ClientSideRowModelModule, ValidationModule } from "ag-grid-community";
import "./GridStyles.css";
import { themeBalham } from "ag-grid-community";
import {
  ClientSideRowModelModule,
  PaginationModule,
  ValidationModule,
} from "ag-grid-community";
import {
  ModuleRegistry,
  NumberFilterModule,
  RowSelectionModule,
  TextFilterModule,
  themeQuartz,
} from "ag-grid-community";
function EmployeeAgGrid() {
  const [employees, setEmployees] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [currentPage, setCurrentPage] = useState(0);
  const [pageSize, setPageSize] = useState(50);
  const [totalEmployees, setTotalEmployees] = useState(0);
  const [exporting, setExporting] = useState(false);
  ModuleRegistry.registerModules([
    RowSelectionModule,
    TextFilterModule,
    NumberFilterModule,
    ClientSideRowModelModule,
  ]);
  const modules = [
    PaginationModule,
    ClientSideRowModelModule,
    ...(process.env.NODE_ENV !== "production" ? [ValidationModule] : []),
  ];

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

  const [columnDefs] = useState([
    {
      field: "employeeId",
      filter: true,
    },
    {
      field: "firstName",
      filter: true,
    },
    {
      field: "lastName",
      filter: true,
    },
    {
      field: "email",
      filter: true,
    },
    {
      field: "phoneNumber",
      filter: true,
    },
    {
      field: "hireDate",
      filter: true,
    },
    {
      field: "dateOfBirth",
      filter: true,
    },
    {
      field: "gender",
      filter: true,
    },
    {
      field: "designation",
      filter: true,
    },
    {
      field: "employmentType",
      filter: true,
    },
    {
      field: "salary",
      filter: true,
    },
    {
      field: "bonus",
      filter: true,
    },
    {
      field: "managerName",
      filter: true,
    },
    {
      field: "managerId",
      filter: true,
    },
    {
      field: "departmentId",
      filter: true,
    },
    {
      field: "department",
      filter: true,
    },
    {
      field: "jobId",
      filter: true,
    },
    {
      field: "address",
      filter: true,
    },
    {
      field: "city",
      filter: true,
    },
    {
      field: "state",
      filter: true,
      headerCheckboxSelection: true,
    },
    {
      field: "country",
      filter: true,
    },
    {
      field: "lastWorkingDate",
      filter: true,
    },
    {
      field: "status",
      filter: true,
    },
  ]);

  const defaultColDef = useMemo(
    () => ({
      flex: 1,
      minWidth: 100,
      filter: true,
    }),
    [],
  );
  const rowSelection = useMemo(() => {
    return {
      mode: "multiRow",
      checkboxes: true,
      headerCheckbox: false,
      enableClickSelection: true,
    };
  }, []);
  const myTheme = themeBalham.withParams({ accentColor: "red" });
  return (
    <div className="container-fluid py-3">
      {/* Top Buttons */}
      <div className="row g-2 mb-3">
        <div className="col-6 col-md-auto">
          <button className="btn btn-primary w-100">Export PDF</button>
        </div>

        <div className="col-6 col-md-auto">
          <button className="btn btn-secondary w-100">Trigger Email</button>
        </div>

        <div className="col-6 col-md-auto">
          <button className="btn btn-info w-100">Trigger Process</button>
        </div>

        <div className="col-6 col-md-auto">
          <button className="btn btn-warning w-100">Edit</button>
        </div>

        <div className="col-6 col-md-auto">
          <button className="btn btn-danger w-100">Delete</button>
        </div>
      </div>

      {/* Grid */}
      <div
        className="ag-theme-balham"
        style={{
          height: "80vh",
          width: "100%",
        }}
      >
        <AgGridReact
          theme={myTheme}
          modules={modules}
          rowData={employees}
          columnDefs={columnDefs}
          defaultColDef={defaultColDef}
          rowSelection={rowSelection}
          pagination={true}
          paginationAutoPageSize={true}
        />
      </div>
    </div>
  );
}

export default EmployeeAgGrid;
