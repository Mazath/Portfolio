-- Username:wiljy107
-- Q1005 List the ProductModel name and Name for all Customers who ordered a product which was the most recent to start selling.No duplicate records!

-- Write your query below

SELECT Product.Name AS productName
	,Person.FirstName + ' ' + Person.LastName AS customersName
FROM Person.Person
RIGHT JOIN Sales.Customer ON Customer.CustomerID = Person.BusinessEntityID
LEFT JOIN Sales.SalesOrderHeader ON SalesOrderHeader.CustomerID = Customer.CustomerID
LEFT JOIN Sales.SalesOrderDetail ON SalesOrderDetail.SalesOrderID = SalesOrderHeader.SalesOrderID
LEFT JOIN Production.Product ON Product.ProductID = SalesOrderDetail.ProductID
WHERE Person.LastName IS NOT NULL
	AND Product.Name IS NOT NULL
	AND Product.SellStartDate = (
		SELECT MAX(SellStartDate)
		FROM Production.Product
		)


--Using the subquary to find the most recent date an item went on sale then calling the subquary in the where clause to apply the conditions 
--to clear out rows with no names 