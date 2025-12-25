**1. 运行后端:**

* 打开一个新的终端。
* 导航到 [backend](vscode-file://vscode-app/d:/Microsoft%20VS%20Code/resources/app/out/vs/code/electron-browser/workbench/workbench.html) 目录: `cd backend`
* 运行 Spring Boot 应用程序: `./mvnw spring-boot:run`

**2. 运行前端:**

* 打开另一个新的终端。
* 导航到 [frontend](vscode-file://vscode-app/d:/Microsoft%20VS%20Code/resources/app/out/vs/code/electron-browser/workbench/workbench.html) 目录: `cd frontend`
* 运行 Vue.js 开发服务器: `npm run dev`

**3. 访问应用程序:**

* 在浏览器中打开 `http://localhost:5173`。
* 您将看到登录页面。使用以下凭据登录:
  * **用户名:** admin
  * **密码:** password
* 登录后，您将被重定向到学生管理页面，您可以在其中对学生信息进行增删改查操作。

目前完成：功能實現、AOP緩存登錄信息并在前端顯示、數據持久化保存

數據庫：root 密碼123456 創建參考database\mysql-init.sql
