alter session set "_ORACLE_SCRIPT"=true;
create user movie_manager_db identified by "movie_manager_db";
grant sysdba to movie_manager_db;
grant all privileges to movie_manager_db;
exit;