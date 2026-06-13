import axios from "axios";

const EMPLOYEE_API_BASE_URL = "http://localhost:9092/employeeAPI";
const DOC_API_BASE_URL = process.env.REACT_APP_API_BASE_URL || "";

const getEmployees = async (pageNumber = 0, pageSize = 50) => {
  try {
    const response = await axios.get(
      `${EMPLOYEE_API_BASE_URL}/paged?page=${pageNumber}&size=${pageSize}`,
    );
    return response.data;
  } catch (error) {
    throw error;
  }
};

// const extractEmployeesPdf = async () => {
//   try {
//     const response = await axios.get(`${EMPLOYEE_API_BASE_URL}/export/pdf`, {
//       responseType: "blob",
//     });
//     return response.data;
//   } catch (error) {
//     throw error;
//   }
// };

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
};
