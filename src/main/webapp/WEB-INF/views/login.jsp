<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html><html lang="vi"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1"><title>Đăng nhập - ShopMan</title><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/shopman.css"></head>
<body><div class="login-wrap"><form class="login-card" method="post">
  <h1>ĐĂNG NHẬP SHOPMAN</h1><p>Hệ thống quản lý bán hàng bách hóa trực tuyến</p>
  <% if(request.getAttribute("error")!=null){ %><div class="flash error"><%= request.getAttribute("error") %></div><% } %>
  <label>Tên đăng nhập</label><input class="input" name="username" required autofocus>
  <label>Mật khẩu</label><input class="input" type="password" name="password" required>
  <button class="btn">ĐĂNG NHẬP</button>
  <div class="hint">Demo: manager / 123456 &nbsp;·&nbsp; customer / 123456 &nbsp;·&nbsp; sales / 123456</div>
</form></div></body></html>
