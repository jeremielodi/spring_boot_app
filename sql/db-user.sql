CREATE USER 'digipay_app_user'@'localhost' IDENTIFIED BY 'd#g#p@y?2023';
GRANT ALL PRIVILEGES ON *.* TO 'digipay_app_user'@'localhost';
ALTER USER 'digipay_app_user'@'localhost' IDENTIFIED WITH mysql_native_password BY 'd#g#p@y?2023';
FLUSH PRIVILEGES;
