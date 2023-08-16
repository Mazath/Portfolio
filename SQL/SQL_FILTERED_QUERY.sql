
-- Q1002  Write a query to display the First and Last name of all employees that currently work in the Tool Design department as part of the research 
-- and development group whose personal details were modified no later than the end 2004

-- Write your query below


SELECT FirstName
	,LastName
FROM Person.Person p
LEFT JOIN HumanResources.EmployeeDepartmentHistory h ON p.BusinessEntityID = h.BusinessEntityID
WHERE h.DepartmentID = 2
	AND p.ModifiedDate > 2004


--DepartmentID set to 2 as it is the tool design department ID value, 
--this was done so if the name of the department changes, the quary would still return the correct list of employees.

