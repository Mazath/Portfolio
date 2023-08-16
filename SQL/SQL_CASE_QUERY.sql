-- Q1009 write a query that returns the scheduled start and end dates, actual start and end dates and two additional descriptive 
-- columns that explains the start and end dates of the various work order routes.
--
-- The two additional colums should display:
-- 1. "started late" if the actual start date is after the scheduled start date
-- 2. "started early" if the actual start date is before the scheduled start date
-- 3. "started on time" if Production started when scheduled.

-- 1. "finished late" if the actual end date is after the scheduled end date
-- 2. "finished early" if the actual end date is before the scheduled end date
-- 3. "finished on time" if Production finished when scheduled.

-- Schemas: Production

-- Example output:
-- ScheduledStartDate	ActualStartDate	ScheduledEndDate	ActualEndDate	startDescription	endDescription
-- 2011-06-05 			2011-06-05 		2011-06-16 			2011-06-23 		Started on time		Finished late
-- 2011-06-06 			2011-06-13 		2011-06-17 			2011-06-17 		Started late		Finished on time
-- 2011-06-07 			2011-06-07 		2011-06-18 			2011-06-27 		Started on time		Finished late
-- 2011-06-08 			2011-06-17 		2011-06-19 			2011-06-19 		Started late		Finished on time


SELECT WOR.ScheduledStartDate
	,WO.StartDate AS ActualStartDate
	,wor.ScheduledEndDate
	,wo.EndDate AS ActualEndDate
	,(
		CASE 
			WHEN (WOR.ScheduledStartDate > WO.StartDate)
				THEN 'Started late'
			WHEN (WOR.ScheduledStartDate < WO.StartDate)
				THEN 'started early'
			WHEN (WOR.ScheduledStartDate = WO.StartDate)
				THEN 'started on time'
			END
		) AS startDescription
	,(
		CASE 
			WHEN (wor.ScheduledEndDate > wo.EndDate)
				THEN 'finished late'
			WHEN (wor.ScheduledEndDate < wo.EndDate)
				THEN 'finished early'
			WHEN (wor.ScheduledEndDate = wo.EndDate)
				THEN 'finished on time'
			END
		) AS endDescription
FROM Production.WorkOrder WO
RIGHT JOIN Production.WorkOrderRouting WOR ON WOR.WorkOrderID = WO.WorkOrderID

