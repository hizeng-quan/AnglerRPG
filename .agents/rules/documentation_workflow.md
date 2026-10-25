# Quy Định Bắt Buộc Cập Nhật Tài Liệu & Bản Tin (Documentation & Patch Notes Workflow)

> **Quy tắc bất di bất dịch:** Mọi cập nhật code (sửa lỗi, cân bằng, đồ họa, gameplay, tính năng mới) **BẮT BUỘC** phải được ghi chép đồng bộ vào 3 vị trí sau trước khi nghiệm thu:

---

## 1. `CHANGELOG.md`
- Ghi lại số phiên bản mới (hoặc cập nhật phiên bản hiện tại).
- Phân nhóm rõ ràng theo các mục:
  * `Sửa Lỗi (Bug Fixes)`
  * `Cân Bằng (Game Balance & Numerical Tweaks)`
  * `Giao Diện & Trải Nghiệm (UI / UX / Polish)`
  * `Tính Năng Mới (New Features)`
- Nêu rõ nguyên nhân kỹ thuật (Root Cause) và phương án xử lý để phục vụ tra cứu sau này.

---

## 2. `TASKS.md`
- Cập nhật Bảng Tổng Hợp Mức Độ Ưu Tiên (Priority Matrix):
  * Cập nhật trạng thái `✅ Đã khắc phục triệt để` hoặc `✅ Đã hoàn thành`.
- Bổ sung chi tiết mã lỗi (ví dụ: `BUG-07`, `BAL-09`, `UI-02`):
  * Hiện tượng (Symptom)
  * Phân tích nguyên nhân (Root Cause)
  * Giải pháp thực hiện (Resolution)

---

## 3. Bản Tin Cập Nhật In-Game (`com.fishingrpg.game.ui.PatchNotesDialog.java`)
- Bổ sung khối phiên bản mới vào danh sách `patchNotesItems`:
  ```text
  patchNotesItems.add(UpdateItem.versionHeader("PHIÊN BẢN vX.X.X (...)"));
  patchNotesItems.add(new UpdateItem("HẠNG MỤC", "Tiêu Đề Tóm Tắt", badgeColor)
      .addPoint("Chi tiết thay đổi 1...")
      .addPoint("Chi tiết thay đổi 2...")
  );
  ```
- Đảm bảo người chơi khi khởi động game và mở Bảng Cài đặt / Bản Tin sẽ nắm bắt được ngay các thay đổi mới nhất mà không cần đọc mã nguồn.
