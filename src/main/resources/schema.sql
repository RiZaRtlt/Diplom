drop table my_User;
drop table token;
drop table files;

create table my_User(
                        id int IDENTITY(1,1),
                        login varchar(50) not null,
                        password varchar(50) not null,
                        PRIMARY KEY(id)
);

create table token(
                        login varchar(50) not null,
                        token varchar(100) not null,
                        PRIMARY KEY(login)
);

create table files(
                      id int IDENTITY(1,1),
                      userName varchar(50) not null,
                      fileName varchar(250) not null,
                      size int not null,
                      fileData varchar(MAX) not null
                      PRIMARY KEY(id)
);