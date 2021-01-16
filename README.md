# Delivery Management
this repo copy from my bitbucket.

---

#### Folder Structure
 ```
data                            # ไฟล์ data เอาไว้เก็บข้อมูลชื่อบัญชี ผู้เช่า ห้องและอื่นๆ\
src/main\
└───java\
    └───delivery.management    # Project Package\
        └───controller         # รวมไฟล์ Controller ที่เชื่อมต่อกับหน้า view แบบ 1:1 โดยชื่อของ Controller จะมีความเหมือนกับชื่อไฟล์ view fxml\
        └───model              # เก็บรวบรวม Model Object ทั้งหมดที่ใช้ในโปรเจ็ค และมีการจัดการไฟล์ที่นี่\
        └───Installer.java     # เป็น Class พิเศษที่ไว้ใช้ตรวจจับกรณีไม่พบ Folder Data ที่ใช้กับโปรเจ็ครวมถึงสร้างขึ้นใหม่ในกรณีที่ไม่พบ\
        └───Main.java          # ไฟล์ Main เป็นไฟล์ตั้งต้นสำหรับ RUN โปรเจ็ค\
└───resources       # เก็บไฟล์ view และรูปภาพสำหรับโปรเจ็ค\
    └───asset       # เก็บไฟล์เพิ่มเติมเกี่ยวกับวิธีการใช้โปรแกรมและรูปภาพ\
    └───view        # เป็น Folder ที่เก็บ view ทั้งหมดไว้ แทบทั้งหมดเป็นไฟล์ fxml ยกเว้นบางส่วนที่เป็นรูป\
 ```