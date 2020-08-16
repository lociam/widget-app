CREATE TABLE widgets(
id VARCHAR(36) default random_uuid() primary key,
x int,
y int,
z_index int,
width int,
height int
);

CREATE UNIQUE INDEX z_index_idx ON widgets (z_index);