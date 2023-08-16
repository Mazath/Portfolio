
-- Q1010 List the different types of people in the person table, the count of people of that type and an 
-- additional descriptive colum that clearly shows what each person type code represents.  
-- Order the records from lowest to highest count.  
-- For the person types use: SC = Store Contact, IN = Individual (retail) customer, 
--		SP = Sales person, EM = Employee (non-sales), VC = Vendor contact, GC = General contact
-- Schemas: Person

-- Example output:
-- personType	personCount	personTypeDesc
-- SP			17			Sales Person
-- VC			156			Vendor contact
 
SELECT PersonType
	,count(*) personCount
	,(
		CASE 
			WHEN (Person.Person.PersonType = 'EM')
				THEN 'Employee (non-sales)'
			WHEN (Person.Person.PersonType = 'SP')
				THEN 'Sales person'
			WHEN (Person.Person.PersonType = 'SC')
				THEN 'Store Contact'
			WHEN (Person.Person.PersonType = 'VC')
				THEN 'Vendor contact'
			WHEN (Person.Person.PersonType = 'GC')
				THEN 'General contact'
			WHEN (Person.Person.PersonType = 'IN')
				THEN 'Individual (retail) customer'
			END
		) AS personTypeDesc
FROM Person.Person
GROUP BY personType
ORDER BY personCount


--case clause assesses the value in the persontype column and populates personTypeDesc based on value 