import React, {
  useCallback,
  useContext,
  useEffect,
  useMemo,
  useRef,
  useState,
} from "react";
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
import { CounterContext } from "../App";
import { useNavigate } from "react-router-dom";
function EmployeeAgGrid() {
  const [employees, setEmployees] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [currentPage, setCurrentPage] = useState(0);
  const [pageSize, setPageSize] = useState(50);
  const [totalEmployees, setTotalEmployees] = useState(0);
  const [exporting, setExporting] = useState(false);
  const [selectedRows, setSelectedRows] = useState({});
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

  const getEmployees = useCallback(async () => {
    const response = await APIServices.getEmployees(currentPage, pageSize);

    const data = await response.json();

    setEmployees(data);
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
  const navigation=useNavigate();
  // const { employeeContext, setEmployeeContext } = useContext(CounterContext);
  // const { empContext, setEmpContext } = useContext(CounterContext);
  const onRowSelected = useCallback((event) => {
    if (event.node.isSelected()) {
      let rowInfo = {
        employeeId: event.data.employeeId,
        firstName: event.data.firstName,
        lastName: event.data.lastName,
        email: event.data.email,
        phoneNumber: event.data.phoneNumber,
        gender: event.data.gender,
        dateOfBirth: event.data.dateOfBirth,
        address: event.data.address,
        city: event.data.city,
        state: event.data.state,
        country: event.data.country,
        designation: event.data.designation,
        jobId: event.data.jobId,
        departmentId: event.data.departmentId,
        // department: "",
        managerName: event.data.managerName,
        managerId: event.data.managerId,
        hireDate: event.data.hireDate,
        lastWorkingDate: event.data.lastWorkingDate,
        employmentType: event.data.employmentType,
        status: event.data.status,
        localStatus: event.data.localStatus,
        salary: event.data.salary,
        bonus: event.data.bonus,
      };

      setSelectedRows(rowInfo);
    }
  }, []);

  const transferData =()=>{
    navigation("/form",{
      state:selectedRows
    })
  }
  return (
    <div className="container-fluid">
      {/* Top Buttons */}
      <div className="row g-2 mb-3">
        <div className="col-6 col-md-auto">
          Page size:
          <select
            className="form-select"
            value={pageSize}
            onChange={(e) => setPageSize(Number(e.target.value))}
          >
            <option value={10}>10</option>
            <option value={20}>20</option>
            <option value={50}>50</option>
            <option value={100}>100</option>
            <option value={200}>200</option>
          </select>
        </div>

        <div className="col-6 col-md-auto">
          <button className="btn btn-secondary w-100" onClick={transferData}>Navigate</button>
        </div>

        <div className="col-6 col-md-auto">
          <button className="btn btn-info w-100">Trigger Process</button>
        </div>

        <div className="col-6 col-md-auto">
          <button
            className="btn btn-warning w-100"
            onClick={() => setCurrentPage((prev) => prev - 1)}
          >
            Prev
          </button>
        </div>

        <div className="col-6 col-md-auto">
          <button
            className="btn btn-danger w-100"
            onClick={() => setCurrentPage((prev) => prev + 1)}
          >
            Next
          </button>
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
          onRowSelected={onRowSelected}
        />
      </div>
    </div>
  );
}

export default EmployeeAgGrid;
