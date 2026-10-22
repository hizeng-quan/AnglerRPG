@echo off
chcp 65001 > nul
title Angler RPG - Huyền Thoại Cần Thủ
cls

echo ========================================================
echo        ANGLER RPG - HUYỀN THOẠI CẦN THỦ
echo ========================================================
echo.

:: Kiểm tra cập nhật đang chờ xử lý
if exist "AnglerRPG-update.tmp" (
    echo [Updater] Phát hiện bản cập nhật mới chưa áp dụng. Đang hoàn tất cập nhật...
    if exist "AnglerRPG.jar" del /f /q "AnglerRPG.jar"
    move /y "AnglerRPG-update.tmp" "AnglerRPG.jar" > nul
    echo [Updater] Đã cập nhật thành công!
    echo.
)

:: Kiểm tra file JAR game chính
if not exist "AnglerRPG.jar" (
    if exist "lwjgl3\build\libs\FishingRPG-1.0.0.jar" (
        copy /y "lwjgl3\build\libs\FishingRPG-1.0.0.jar" "AnglerRPG.jar" > nul
    ) else (
        echo [Lỗi] Không tìm thấy tệp AnglerRPG.jar!
        echo Vui lòng chạy lệnh: gradlew lwjgl3:jar để đóng gói game trước.
        pause
        exit /b 1
    )
)

:: Kiểm tra môi trường Java
java -version > nul 2>&1
if %errorlevel% neq 0 (
    echo [Lỗi] Máy tính chưa cài đặt Java SE Runtime Environment (JRE/JDK 11+)!
    echo Vui lòng tải và cài đặt Java tại: https://www.oracle.com/java/technologies/downloads/
    pause
    exit /b 1
)

echo [Khởi Chạy] Đang mở Angler RPG...
start "" javaw -Xms256m -Xmx1024m -jar "AnglerRPG.jar" || java -Xms256m -Xmx1024m -jar "AnglerRPG.jar"
exit
