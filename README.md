# javademo06
javademo06
# 📘 自助租書系統 Self-Service Book Rental System (javademo06)

這是一個以 **Java Swing + MySQL + Maven** 製作的桌面應用程式，  
模擬實體租書店的「會員登入、租書、還書、儲值、匯出租借明細」完整流程。  
本專案採用 **三層架構 (DAO / Service / Controller)**，是入門 Java 專案結構的最佳範例。

---

## 🧩 專案架構 Project Structure

```text
src/main/java/
 ├─ controller/        ← Swing UI（LoginFrame、MainFrame）
 ├─ po/                ← 資料物件（Book、Member、TopupLog）
 │   ├─ dao/           ← 介面層
 │   │   └─ impl/      ← DAO 實作
 │   └─ service/       ← 業務邏輯層
 │       └─ impl/      ← Service 實作
 ├─ util/              ← 工具類（DbConnection、ExcelExporter）
 ├─ config/            ← 保留資料夾
 ├─ exception/         ← 自訂例外
 └─ sql/schema.sql     ← 建立資料表與測試資料
```
## ⚙️ 環境需求 Requirements
| 項目                | 說明                                          |
| ----------------- | ------------------------------------------- |
| ☕ **JDK**         | Java 17 以上                                  |
| 🧩 **IDE**        | Eclipse 2025-09 (4.37.0)                    |
| 🗃 **Database**   | MySQL 8.0+                                  |
| 📦 **Build Tool** | Maven（內含 junit、mysql-connector-j、poi-ooxml） |

## 🧱 資料庫設定 Database Setup
```sql
CREATE DATABASE IF NOT EXISTS javademo06 CHARACTER SET utf8mb4;
USE javademo06;

CREATE TABLE member(
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(50),
  username VARCHAR(50) UNIQUE,
  password VARCHAR(100),
  balance DECIMAL(10,2)
);

CREATE TABLE book(
  id INT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(100),
  price DECIMAL(10,2),
  stock INT
);

CREATE TABLE rentlog(
  id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50),
  bookid INT,
  action VARCHAR(10),
  time VARCHAR(40)
);

CREATE TABLE topuplog(
  id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50),
  amount DECIMAL(10,2),
  balance DECIMAL(10,2),
  time VARCHAR(40)
);

INSERT INTO member(name, username, password, balance)
VALUES ('小明','user1','1234',100);

INSERT INTO book(title, price, stock)
VALUES ('Java入門',50,3),('資料庫概論',60,2),('演算法圖解',70,1);
```
## 2️⃣ 修改資料庫連線設定
```java
private static final String URL  = "jdbc:mysql://localhost:3306/javademo06?useSSL=false&serverTimezone=Asia/Taipei";
private static final String USER = "root";   // ← 改這裡
private static final String PASS = "1234";   // ← 改這裡
```
## 🚀 執行方式 How to Run
```css
src/main/java/controller/LoginFrame.java
```

## 🖥️ 系統畫面 UI Overview
| 登入畫面 LoginFrame          | 主畫面 MainFrame          |
| ------------------------ | ---------------------- |
| ![login](docs/login.png) | ![main](docs/main.png) |

## 🪄 功能說明 Features
| 功能                | 說明                                        |
| ----------------- | ----------------------------------------- |
| 🧍‍♂️ **登入 / 離開** | 使用帳號密碼登入系統（例：user1 / 1234）                |
| 📚 **書籍清單**       | 顯示所有書籍、價格、庫存                              |
| 🛒 **租書**         | 選取表格一筆 → 按「租書」加入下方明細                      |
| ✅ **確定租書？**       | 實際扣款、更新庫存、寫入資料庫                           |
| 🔁 **還書**         | 輸入書籍 ID → 加回庫存                            |
| 💰 **儲值**         | 輸入現金金額 → 增加餘額                             |
| 📈 **儲值金歷程**      | 顯示租書與儲值的所有交易紀錄                            |
| 📤 **匯出明細**       | 匯出**當次租借**紀錄成 Excel 檔 `current_rent.xlsx` |
| ⏰ **時間顯示**        | 自動每秒更新「時間」欄位                              |

## 📦 匯出檔案範例 Export Example
| 欄位 | 範例資料                |
| -- | ------------------- |
| 帳號 | **user1**           |
| 時間 | 2025/10/20 15:05:00 |

| 序號 | 書名     | 價格 |
|------|----------|------|
| 1    | Java入門 | 50   |
| 2    | 資料庫概論 | 60   |
| **總金額** | &nbsp; | **110** |



