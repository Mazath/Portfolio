-- Q1008 Find the most expensive product(s) from those that have had 3 or more price changes
-- Schemas: Production

-- Example output
-- ProductID	Name						ProductNumber	StandardCost	ListPrice
-- 680			HL Road Frame - Black, 58	FR-R92B-58		1059.31			1431.50
-- 706			HL Road Frame - Red, 58		FR-R92R-58		1059.31			1431.50


SELECT Product.ProductID
	,Name
	,ProductNumber
	,Product.StandardCost
	,ListPrice
FROM Production.Product
WHERE ListPrice = (
		SELECT MAX(ListPrice)
		FROM Production.ProductListPriceHistory
		WHERE productID IN (
				SELECT ProductID
				FROM Production.ProductListPriceHistory
				GROUP BY ProductID
				HAVING COUNT(*) >= 3
				)
		)
	AND productID IN (
		SELECT ProductID
		FROM Production.ProductListPriceHistory
		GROUP BY ProductID
		HAVING COUNT(*) >= 3
		)
