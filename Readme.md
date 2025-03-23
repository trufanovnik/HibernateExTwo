# Асинхронная работа с таблицей (Hibernate)

Скрипт создает таблицу согласно сущности Items, добавляет в нее 40 строк со значением val = 0. 
Далее запускается пул потоков, каждый из которых увеличивает значение val на единицу в "случайно" взятой строке (каждый тред должен изменить данные определенное количество раз(напр, 20000))
Для синхронизации данных использовался Optimistic lock. 

---

## Требования

- Java 8 или выше
- PostgreSQL
- Hibernate
- Maven (для сборки проекта)

---

## Установка и запуск

1. **Установите PostgreSQL:**
    - Создайте базу данных с именем `your_database_name`.
    - Обновите настройки подключения в файле `hibernate.cfg.xml`:
      ```xml
      <property name="hibernate.connection.url">jdbc:postgresql://localhost:5432/your_database_name</property>
      <property name="hibernate.connection.username">your_username</property>
      <property name="hibernate.connection.password">your_password</property>
      ```
      
2. **Соберите проект:**
    - Используйте Maven для сборки проекта:
      ```bash
      mvn clean install
      ```

3. **Запустите приложение:**
    - Запустите приложение с помощью команды:
      ```bash
      java -jar target/your-app-name.jar
      ```
