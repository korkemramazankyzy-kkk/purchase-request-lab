CREATE TABLE IF NOT EXISTS purchase_request (                                  id UUID PRIMARY KEY,
                                  business_key TEXT NOT NULL UNIQUE,
                                  status TEXT NOT NULL,
                                  title TEXT NOT NULL,
                                  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                                  CONSTRAINT purchase_request_status_known
                                      CHECK (status IN ('DRAFT', 'APPROVED', 'ORDERED', 'RECEIVED'))
);
