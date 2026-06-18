import React, { useEffect, useState } from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import "./Form.css";
import { useLocation, useNavigate } from "react-router-dom";
import APIServices from "./APIServices";

function FormPage() {
  const nav = useNavigate();
  const location=useLocation();
  const employee = location.state;
  const [formData, setFormData] = useState({
    employeeId: "",
    firstName: "",
    lastName: "",
    email: "",
    phoneNumber: "",
    gender: "",
    dateOfBirth: "",
    address: "",
    city: "",
    state: "",
    country: "",
    designation: "",
    jobId: "",
    departmentId: "",
    // department: "",
    managerName: "",
    managerId: "",
    hireDate: "",
    lastWorkingDate: "",
    employmentType: "",
    status: "",
    localStatus: "",
    salary: "",
    bonus: "",
  });

  const [errors, setErrors] = useState({});

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
    // Clear error for this field when user starts typing
    if (errors[name]) {
      setErrors((prev) => ({
        ...prev,
        [name]: "",
      }));
    }
  };

  const validateForm = () => {
    const newErrors = {};
    if (!formData.firstName.trim())
      newErrors.firstName = "First name is required";
    if (!formData.lastName.trim()) newErrors.lastName = "Last name is required";
    if (!formData.email.trim()) newErrors.email = "Email is required";
    if (formData.email && !formData.email.includes("@"))
      newErrors.email = "Invalid email format";
    if (!formData.phoneNumber)
      newErrors.phoneNumber = "Phone number is required";
    if (!formData.hireDate) newErrors.hireDate = "Hire date is required";
    if (!formData.designation.trim())
      newErrors.designation = "Designation is required";

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    // if (validateForm()) {
    //   console.log("Form submitted:", formData);
    //   // Here you can add API call to submit the form
    //   alert("Form submitted successfully!");
    // }
    // nav("/employees");
    fetchEmployees();
  };
  const fetchEmployees = async () => {
    try {
      const data = await APIServices.createEmployee(formData);
      if (data.employeeId !== undefined) {
        alert("Employee created successfully!");
        nav("/employees");
      }
    } catch (err) {
      // setError(err.message || "Unable to load employees.");
    } finally {
      // setLoading(false);
    }
  };

  const handleReset = () => {
    setFormData({
      employeeId: "",
      firstName: "",
      lastName: "",
      email: "",
      phoneNumber: "",
      gender: "",
      dateOfBirth: "",
      address: "",
      city: "",
      state: "",
      country: "",
      designation: "",
      jobId: "",
      departmentId: "",

      managerName: "",
      managerId: "",
      hireDate: "",
      lastWorkingDate: "",
      employmentType: "",
      status: "",
      localStatus: "",
      salary: "",
      bonus: "",
    });
    setErrors({});
  };
useEffect(()=>{
  if(employee.employeeId != undefined){
    setFormData(employee);
  }
},[])
  return (
    <div className="form-container">
      <div className="container mt-5 mb-5">
        <div className="row justify-content-center">
          <div className="col-lg-10">
            <div className="card shadow-lg">
              <div className="card-header bg-primary text-white">
                <h3 className="mb-0">Employee Form</h3>
              </div>
              <div className="card-body p-4">
                <form>
                  {/* Personal Information Section */}
                  <div className="form-section mb-4">
                    <h5 className="section-title border-bottom pb-2 mb-3">
                      Personal Information
                    </h5>
                    <div className="row">
                      <div className="col-md-6 mb-3">
                        <label
                          htmlFor="firstName"
                          className="form-label fw-bold"
                        >
                          First Name <span className="text-danger">*</span>
                        </label>
                        <input
                          type="text"
                          className={`form-control ${
                            errors.firstName ? "is-invalid" : ""
                          }`}
                          id="firstName"
                          name="firstName"
                          value={formData.firstName}
                          onChange={handleChange}
                          placeholder="Enter first name"
                        />
                        {errors.firstName && (
                          <div className="invalid-feedback">
                            {errors.firstName}
                          </div>
                        )}
                      </div>
                      <div className="col-md-6 mb-3">
                        <label
                          htmlFor="lastName"
                          className="form-label fw-bold"
                        >
                          Last Name <span className="text-danger">*</span>
                        </label>
                        <input
                          type="text"
                          className={`form-control ${
                            errors.lastName ? "is-invalid" : ""
                          }`}
                          id="lastName"
                          name="lastName"
                          value={formData.lastName}
                          onChange={handleChange}
                          placeholder="Enter last name"
                        />
                        {errors.lastName && (
                          <div className="invalid-feedback">
                            {errors.lastName}
                          </div>
                        )}
                      </div>
                    </div>

                    <div className="row">
                      <div className="col-md-6 mb-3">
                        <label htmlFor="email" className="form-label fw-bold">
                          Email <span className="text-danger">*</span>
                        </label>
                        <input
                          type="email"
                          className={`form-control ${
                            errors.email ? "is-invalid" : ""
                          }`}
                          id="email"
                          name="email"
                          value={formData.email}
                          onChange={handleChange}
                          placeholder="Enter email"
                        />
                        {errors.email && (
                          <div className="invalid-feedback">{errors.email}</div>
                        )}
                      </div>
                      <div className="col-md-6 mb-3">
                        <label
                          htmlFor="phoneNumber"
                          className="form-label fw-bold"
                        >
                          Phone Number <span className="text-danger">*</span>
                        </label>
                        <input
                          type="tel"
                          className={`form-control ${
                            errors.phoneNumber ? "is-invalid" : ""
                          }`}
                          id="phoneNumber"
                          name="phoneNumber"
                          value={formData.phoneNumber}
                          onChange={handleChange}
                          placeholder="Enter phone number"
                        />
                        {errors.phoneNumber && (
                          <div className="invalid-feedback">
                            {errors.phoneNumber}
                          </div>
                        )}
                      </div>
                    </div>

                    <div className="row">
                      <div className="col-md-6 mb-3">
                        <label htmlFor="gender" className="form-label fw-bold">
                          Gender
                        </label>
                        <select
                          className="form-control"
                          id="gender"
                          name="gender"
                          value={formData.gender}
                          onChange={handleChange}
                        >
                          <option value="">Select Gender</option>
                          <option value="Male">Male</option>
                          <option value="Female">Female</option>
                          <option value="Other">Other</option>
                        </select>
                      </div>
                      <div className="col-md-6 mb-3">
                        <label
                          htmlFor="dateOfBirth"
                          className="form-label fw-bold"
                        >
                          Date of Birth
                        </label>
                        <input
                          type="date"
                          className="form-control"
                          id="dateOfBirth"
                          name="dateOfBirth"
                          value={formData.dateOfBirth}
                          onChange={handleChange}
                        />
                      </div>
                    </div>
                  </div>

                  {/* Address Section */}
                  <div className="form-section mb-4">
                    <h5 className="section-title border-bottom pb-2 mb-3">
                      Address Information
                    </h5>
                    <div className="row">
                      <div className="col-md-12 mb-3">
                        <label htmlFor="address" className="form-label fw-bold">
                          Address
                        </label>
                        <input
                          type="text"
                          className="form-control"
                          id="address"
                          name="address"
                          value={formData.address}
                          onChange={handleChange}
                          placeholder="Enter street address"
                        />
                      </div>
                    </div>

                    <div className="row">
                      <div className="col-md-6 mb-3">
                        <label htmlFor="city" className="form-label fw-bold">
                          City
                        </label>
                        <input
                          type="text"
                          className="form-control"
                          id="city"
                          name="city"
                          value={formData.city}
                          onChange={handleChange}
                          placeholder="Enter city"
                        />
                      </div>
                      <div className="col-md-6 mb-3">
                        <label htmlFor="state" className="form-label fw-bold">
                          State
                        </label>
                        <input
                          type="text"
                          className="form-control"
                          id="state"
                          name="state"
                          value={formData.state}
                          onChange={handleChange}
                          placeholder="Enter state"
                        />
                      </div>
                    </div>

                    <div className="row">
                      <div className="col-md-6 mb-3">
                        <label htmlFor="country" className="form-label fw-bold">
                          Country
                        </label>
                        <input
                          type="text"
                          className="form-control"
                          id="country"
                          name="country"
                          value={formData.country}
                          onChange={handleChange}
                          placeholder="Enter country"
                        />
                      </div>
                    </div>
                  </div>

                  {/* Employment Information Section */}
                  <div className="form-section mb-4">
                    <h5 className="section-title border-bottom pb-2 mb-3">
                      Employment Information
                    </h5>
                    <div className="row">
                      <div className="col-md-6 mb-3">
                        <label
                          htmlFor="designation"
                          className="form-label fw-bold"
                        >
                          Designation <span className="text-danger">*</span>
                        </label>
                        <input
                          type="text"
                          className={`form-control ${
                            errors.designation ? "is-invalid" : ""
                          }`}
                          id="designation"
                          name="designation"
                          value={formData.designation}
                          onChange={handleChange}
                          placeholder="Enter designation"
                        />
                        {errors.designation && (
                          <div className="invalid-feedback">
                            {errors.designation}
                          </div>
                        )}
                      </div>
                      <div className="col-md-6 mb-3">
                        <label htmlFor="jobId" className="form-label fw-bold">
                          Job ID
                        </label>
                        <input
                          type="number"
                          className="form-control"
                          id="jobId"
                          name="jobId"
                          value={formData.jobId}
                          onChange={handleChange}
                          placeholder="Enter job ID"
                        />
                      </div>
                    </div>

                    <div className="row">
                      <div className="col-md-6 mb-3">
                        <label
                          htmlFor="employmentType"
                          className="form-label fw-bold"
                        >
                          Employment Type
                        </label>
                        <select
                          className="form-control"
                          id="employmentType"
                          name="employmentType"
                          value={formData.employmentType}
                          onChange={handleChange}
                        >
                          <option value="">Select Employment Type</option>
                          <option value="Full-time">Full-time</option>
                          <option value="Part-time">Part-time</option>
                          <option value="Contract">Contract</option>
                          <option value="Temporary">Temporary</option>
                        </select>
                      </div>
                      <div className="col-md-6 mb-3">
                        <label htmlFor="status" className="form-label fw-bold">
                          Status
                        </label>
                        <select
                          className="form-control"
                          id="status"
                          name="status"
                          value={formData.status}
                          onChange={handleChange}
                        >
                          <option value="">Select Status</option>
                          <option value="Active">Active</option>
                          <option value="Inactive">Inactive</option>
                          <option value="On Leave">On Leave</option>
                        </select>
                      </div>
                    </div>

                    <div className="row">
                      <div className="col-md-6 mb-3">
                        <label
                          htmlFor="localStatus"
                          className="form-label fw-bold"
                        >
                          Local Status
                        </label>
                        <select
                          className="form-control"
                          id="localStatus"
                          name="localStatus"
                          value={formData.localStatus}
                          onChange={handleChange}
                        >
                          <option value="">Select Local Status</option>
                          <option value="Probation">Probation</option>
                          <option value="Confirmed">Confirmed</option>
                          <option value="Notice Period">Notice Period</option>
                        </select>
                      </div>
                      <div className="col-md-6 mb-3">
                        <label
                          htmlFor="departmentId"
                          className="form-label fw-bold"
                        >
                          Department ID
                        </label>
                        <input
                          type="number"
                          className="form-control"
                          id="departmentId"
                          name="departmentId"
                          value={formData.departmentId}
                          onChange={handleChange}
                          placeholder="Enter department ID"
                        />
                      </div>
                    </div>

                    <div className="row">
                      <div className="col-md-6 mb-3">
                        <label
                          htmlFor="managerName"
                          className="form-label fw-bold"
                        >
                          Manager Name
                        </label>
                        <input
                          type="text"
                          className="form-control"
                          id="managerName"
                          name="managerName"
                          value={formData.managerName}
                          onChange={handleChange}
                          placeholder="Enter manager name"
                        />
                      </div>
                      <div className="col-md-6 mb-3">
                        <label
                          htmlFor="managerId"
                          className="form-label fw-bold"
                        >
                          Manager ID
                        </label>
                        <input
                          type="number"
                          className="form-control"
                          id="managerId"
                          name="managerId"
                          value={formData.managerId}
                          onChange={handleChange}
                          placeholder="Enter manager ID"
                        />
                      </div>
                    </div>

                    <div className="row">
                      <div className="col-md-6 mb-3">
                        <label
                          htmlFor="hireDate"
                          className="form-label fw-bold"
                        >
                          Hire Date <span className="text-danger">*</span>
                        </label>
                        <input
                          type="date"
                          className={`form-control ${
                            errors.hireDate ? "is-invalid" : ""
                          }`}
                          id="hireDate"
                          name="hireDate"
                          value={formData.hireDate}
                          onChange={handleChange}
                        />
                        {errors.hireDate && (
                          <div className="invalid-feedback">
                            {errors.hireDate}
                          </div>
                        )}
                      </div>
                      <div className="col-md-6 mb-3">
                        <label
                          htmlFor="lastWorkingDate"
                          className="form-label fw-bold"
                        >
                          Last Working Date
                        </label>
                        <input
                          type="date"
                          className="form-control"
                          id="lastWorkingDate"
                          name="lastWorkingDate"
                          value={formData.lastWorkingDate}
                          onChange={handleChange}
                        />
                      </div>
                    </div>
                  </div>

                  {/* Compensation Section */}
                  <div className="form-section mb-4">
                    <h5 className="section-title border-bottom pb-2 mb-3">
                      Compensation Information
                    </h5>
                    <div className="row">
                      <div className="col-md-6 mb-3">
                        <label htmlFor="salary" className="form-label fw-bold">
                          Salary
                        </label>
                        <input
                          type="number"
                          className="form-control"
                          id="salary"
                          name="salary"
                          value={formData.salary}
                          onChange={handleChange}
                          placeholder="Enter salary"
                          step="0.01"
                        />
                      </div>
                      <div className="col-md-6 mb-3">
                        <label htmlFor="bonus" className="form-label fw-bold">
                          Bonus
                        </label>
                        <input
                          type="number"
                          className="form-control"
                          id="bonus"
                          name="bonus"
                          value={formData.bonus}
                          onChange={handleChange}
                          placeholder="Enter bonus"
                          step="0.01"
                        />
                      </div>
                    </div>
                  </div>

                  {/* Form Actions */}
                  <div className="row">
                    <div className="col-12">
                      <div className="d-grid gap-2 d-md-flex justify-content-md-end">
                        <button
                          type="reset"
                          className="btn btn-secondary me-md-2"
                          onClick={handleReset}
                        >
                          Reset
                        </button>
                        <button
                          type="submit"
                          className="btn btn-primary"
                          onClick={(e) => handleSubmit(e)}
                        >
                          Submit
                        </button>
                      </div>
                    </div>
                  </div>
                </form>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default FormPage;
