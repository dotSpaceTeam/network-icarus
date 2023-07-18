SELECT
  SUM(DATE_PART('ms', end_date - start_date)) AS duration
FROM
	Session
WHERE
	end_date IS NOT NULL
AND
  -- :profile is the profile key.
	Profile = :profile
;