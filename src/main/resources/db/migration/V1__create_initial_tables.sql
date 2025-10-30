CREATE TABLE project (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    startDate DATE,
    endDate DATE
);

CREATE TYPE task_status AS ENUM ('TODO', 'DOING', 'DONE');
CREATE TYPE task_priority AS ENUM ('LOW', 'MEDIUM', 'HIGH');

CREATE TABLE task (
    id SERIAL PRIMARY KEY,
    project_id BIGINT NOT NULL,
    title VARCHAR(150) NOT NULL,
    description TEXT,
    status task_status,
    priority task_priority,
    due_date DATE,

    CONSTRAINT fk_task_project
                  FOREIGN KEY (project_id)
                  REFERENCES project(id)
                  ON DELETE CASCADE
);