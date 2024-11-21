UPDATE functionSchema.functions
SET name        = ?,
    modified_at = CURRENT_TIMESTAMP
WHERE hash_id = ?;