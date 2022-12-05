create table if not exists message (
    time_message timestamp primary key,
    content varchar(100),
    email_source varchar,
    email_dest varchar
);