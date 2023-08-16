
-- Q1001 show the total number of production transactions in the transaction history archive table, the average quantity and the average cost
-- Schemas: Production

-- Example output
-- transCount	avgQuantity avgCost
-- 196			1739		55
-- Write your query below

SELECT COUNT(TransactionID ) AS transCount, AVG(Quantity) AS avgQuantity, AVG(ActualCost) AS  avgCost 
FROM Production.TransactionHistoryArchive 
