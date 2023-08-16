
-- Q1007 Find the most common job title(s) and the number of employees with that title

-- Write your query below


SELECT JobTitle
	,count(jobtitle) AS totalEmployee
FROM HumanResources.Employee
GROUP BY jobtitle
HAVING count(jobtitle) = (
		SELECT max(totalEmployee)
		FROM (
			SELECT JobTitle
				,count(jobtitle) AS totalEmployee
			FROM HumanResources.Employee
			GROUP BY jobtitle
			) jobtitleCount
		)
