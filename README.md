# ZingMP3 Clone

Web app quan ly bai hat xay dung bang Spring MVC + Hibernate + Thymeleaf + MySQL.

## Tech Stack

- Java 21
- Spring MVC 6.2.6
- Spring Data JPA 3.4.2
- Hibernate 6.5.2
- Thymeleaf 3.1.1
- MySQL 8
- Gradle

## Chuc nang

- Danh sach bai hat co phan trang
- Them moi bai hat (upload anh + file audio)
- Chinh sua thong tin bai hat
- Xoa bai hat

## Cai dat va chay du an

### 1. Clone repo

```bash
git clone https://github.com/minhthi2607/zingmp3.git
cd zingmp3
```

### 2. Tao database MySQL

```sql
CREATE DATABASE zingmp3;
```

Hibernate se tu dong tao bang `songs` khi chay ung dung (cau hinh `hbm2ddl.auto = update` trong AppConfiguration).

### 3. Cau hinh ket noi database

Sao chep file mau va dien thong tin cua ban:

```bash
cp src/main/resources/db.properties.example src/main/resources/db.properties
```

Sua file `db.properties`:

```properties
db.url=jdbc:mysql://localhost:3306/zingmp3
db.username=your_mysql_username
db.password=your_mysql_password
```

### 4. Cau hinh thu muc upload

Sua file `src/main/resources/upload_file.properties`:

```properties
file-upload=/path/to/your/uploads/
```

Dam bao thu muc ton tai va co quyen ghi.

### 5. Chay ung dung

```bash
./gradlew build
./gradlew tomcatRun
```

Truy cap: `http://localhost:8080/songs`

## Bao mat file upload

- Ten file duoc sinh ngau nhien bang UUID (tranh Path Traversal)
- Chi cho phep cac dinh dang: anh (JPG, PNG, GIF, WEBP), audio (MP3, OGG, WAV, FLAC)
- MIME type duoc kiem tra phia server
