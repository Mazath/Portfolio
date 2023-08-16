
-- Q1004 For each of the different departments, show the number of current female, male and total employees currently employed.
-- Schemas: HumanResources

-- Example output:
-- departmentName	female	male	totalEmployeeCount
-- Production	     40     120	    	160

-- Write your query below

SELECT DISTINCT Name AS departmentName
	,SUM(CASE 
			WHEN gender = 'F'
				AND EmployeeDepartmentHistory.EndDate IS NULL
				THEN 1
			END) AS Female
	,SUM(CASE 
			WHEN gender = 'M'
				AND EmployeeDepartmentHistory.EndDate IS NULL
				THEN 1
			END) AS Male
	,SUM(CASE 
			WHEN gender IS NOT NULL
				AND EmployeeDepartmentHistory.EndDate IS NULL
				THEN 1
			END) AS totalEmployeeCount
FROM HumanResources.Employee
INNER JOIN HumanResources.EmployeeDepartmentHistory ON EmployeeDepartmentHistory.BusinessEntityID = Employee.BusinessEntityID
INNER JOIN HumanResources.Department ON Department.DepartmentID = EmployeeDepartmentHistory.DepartmentID
GROUP BY (Name)

