import axios from "axios";

const EMPLOYEE_API_BASE_URL = "http://localhost:9092/employeeAPI";
const PROD_ISSUE_API_BASE_URL = "http://localhost:9092/api/prodIssues";

const getEmployees = async (pageNumber, pageSize = 50) => {
  try {
    debugger;
    const response = await axios.get(
      `${PROD_ISSUE_API_BASE_URL}/paged?page=${pageNumber}&size=${pageSize}`,
    );
    return response.data.content;
  } catch (error) {
    throw error;
  }
};

const createEmployee = async (empDetails) => {
  try {
    const response = await axios.post(`${EMPLOYEE_API_BASE_URL}`, empDetails);
    return response.data;
  } catch (error) {
    throw error;
  }
};

const extractEmployeesPdf = async () => {
  try {
    const response = await axios.get(`${EMPLOYEE_API_BASE_URL}/export/pdf`, {
      responseType: "blob",
    });
    const url = window.URL.createObjectURL(new Blob([response.data]));
    const link = document.createElement("a");
    link.href = url;
    link.setAttribute("download", "employees.pdf");
    document.body.appendChild(link);
    link.click();
    link.parentNode.removeChild(link);
  } catch (error) {
    throw error;
  }
};

export default {
  getEmployees,
  extractEmployeesPdf,
  createEmployee,
};
