
-- Q1003 Write a query to display the name and city of all stores that have the word “store” or "supplies" in their name

-- Write your query below



SELECT Name, City
FROM  Sales.Store as s 

inner join Person.BusinessEntity BE
ON BE.BusinessEntityID = s.BusinessEntityID

left join Person.BusinessEntityAddress BEA
on BEA.BusinessEntityID = BE.BusinessEntityID

left join Person.Address PA
on PA.AddressID = BEA.AddressID

where Name like '%store%' or Name like  '%supplies%' 
