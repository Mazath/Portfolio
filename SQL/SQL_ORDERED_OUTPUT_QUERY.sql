-- Username: wiljy107
-- Q1006 List the state/territory and number of home addresses for each of the australian states and territories present in the adventureworks database.  Order the records from lowest to highest number of addresses 
-- Schemas: Person

-- Example output:
-- Name			addressCount
-- Tasmania			100
-- South Australia	200


SELECT StateProvince.Name AS Name
	,count(*) AS addressCount
FROM Person.AddressType
RIGHT JOIN Person.BusinessEntityAddress ON BusinessEntityAddress.AddressTypeID = AddressType.AddressTypeID
INNER JOIN Person.Address ON Address.AddressID = BusinessEntityAddress.AddressID
LEFT JOIN Person.StateProvince ON StateProvince.StateProvinceID = Address.StateProvinceID
LEFT JOIN Person.CountryRegion ON CountryRegion.CountryRegionCode = StateProvince.CountryRegionCode
WHERE AddressType.Name LIKE 'home'
	AND CountryRegion.Name LIKE 'Australia'
GROUP BY StateProvince.Name
ORDER BY addressCount ASC
