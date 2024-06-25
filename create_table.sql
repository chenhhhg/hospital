#建表

drop table if exists t_patient;
CREATE TABLE t_patient (
                        user_id INT primary key auto_increment,
                        user_name varchar(30) unique not null ,
                        password varchar(50) not null ,
                        id_number VARCHAR(18) unique not null ,
                        name VARCHAR(50) NOT NULL,
                        age INT NOT NULL,
                        gender CHAR(1) NOT NULL,
                        address VARCHAR(100) NOT NULL,
                        contact VARCHAR(20) NOT NULL,
                        medical_record TEXT,
                        authorized smallint default 0,
                        index idx_id_number (id_number)
);

drop table if exists t_doctor;
CREATE TABLE t_doctor (
                          user_id INT primary key auto_increment,
                          user_name varchar(30) unique not null ,
                          password varchar(50) not null ,
                          id_number VARCHAR(18) unique not null ,
                          name VARCHAR(50) NOT NULL,
                          age INT NOT NULL,
                          gender CHAR(1) NOT NULL,
                          address VARCHAR(100) NOT NULL,
                          contact VARCHAR(20) NOT NULL,
                          hospital VARCHAR(50) NOT NULL,
                          department VARCHAR(50) NOT NULL,
                          title VARCHAR(20) NOT NULL,
                          specialty VARCHAR(100),
                          authorized smallint default 0,
                          index idx_id_number (id_number)
);

drop table if exists t_admin;
CREATE TABLE t_admin (
                         user_id INT primary key auto_increment,
                         user_name varchar(30) unique not null ,
                         password varchar(50) not null ,
                         id_number VARCHAR(18) unique not null ,
                         name VARCHAR(50) NOT NULL,
                         address VARCHAR(100) NOT NULL,
                         contact VARCHAR(20) NOT NULL,
                         index idx_id_number (id_number)
);

DROP TABLE IF EXISTS `t_log`;
CREATE TABLE `t_log`  (
                          `id` int NOT NULL AUTO_INCREMENT,
                          `admin_id` int NOT NULL,
                          `user_id` int NOT NULL,
                          `user_role` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                          `operation_type` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                          `original_value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                          `modify_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
                          `status` smallint NOT NULL,
                          PRIMARY KEY (`id`) USING BTREE,
                          INDEX `admin_id_idx`(`admin_id` ASC) USING BTREE,
                          INDEX `user_role_id_idx`(`user_role` ASC, `user_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `t_registration`;
CREATE TABLE `t_registration`  (
                                   `id` int NOT NULL AUTO_INCREMENT,
                                   `doctor_id` int NOT NULL,
                                   `quantity` int NOT NULL,
                                   `locked_quantity` int NULL DEFAULT 0,
                                   `date` date NOT NULL,
                                   `daytime` smallint NOT NULL COMMENT '0-morning,1-afternoon',
                                   `authorized` smallint NULL DEFAULT 0 COMMENT '0-no,1-yes',
                                   PRIMARY KEY (`id`) USING BTREE,
                                   INDEX `doctor_id_idx`(`doctor_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `t_registration_relation`;
CREATE TABLE `t_registration_relation`  (
                                            `id` int NOT NULL AUTO_INCREMENT,
                                            `patient_id` int NOT NULL,
                                            `doctor_id` int NOT NULL,
                                            `registration_source` int NOT NULL,
                                            `registration_date` date NOT NULL,
                                            `registration_daytime` smallint NOT NULL,
                                            `pay_status` smallint NOT NULL COMMENT '0-not pay,1-payed',
                                            PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;




INSERT INTO t_patient (user_name, password, id_number, name, age, gender, address, contact, medical_record) VALUES
    ('user1', 'password1', '440305199501010001', '张三', 30, 'M', '广东省深圳市南山区', '13812345678', 'This is the medical record for patient 1.'),
    ('user2', 'password2', '440305199502020002', '李四', 35, 'M', '广东省广州市天河区', '13987654321', 'This is the medical record for patient 2.'),
    ('user3', 'password3', '440305199503030003', '王五', 28, 'F', '湖南省长沙市岳麓区', '18012345678', 'This is the medical record for patient 3.'),
    ('user4', 'password4', '440305199504040004', '赵六', 42, 'M', '江苏省南京市建邺区', '15912345678', 'This is the medical record for patient 4.'),
    ('user5', 'password5', '440305199505050005', '孙七', 25, 'F', '浙江省杭州市西湖区', '13712345678', 'This is the medical record for patient 5.'),
    ('user6', 'password6', '440305199506060006', '周八', 38, 'M', '福建省福州市鼓楼区', '18912345678', 'This is the medical record for patient 6.'),
    ('user7', 'password7', '440305199507070007', '吴九', 31, 'F', '四川省成都市武侯区', '15012345678', 'This is the medical record for patient 7.'),
    ('user8', 'password8', '440305199508080008', '郑十', 27, 'M', '广西壮族自治区南宁市青秀区', '13612345678', 'This is the medical record for patient 8.'),
    ('user9', 'password9', '440305199509090009', '马十一', 45, 'F', '河北省石家庄市长安区', '18712345678', 'This is the medical record for patient 9.'),
    ('user10', 'password10', '440305199510100010', '刘十二', 33, 'M', '山东省青岛市市南区', '15512345678', 'This is the medical record for patient 10.');

INSERT INTO t_doctor (user_name, password, id_number, name, age, gender, address, contact, hospital, department, title, specialty) VALUES
    ('doctor1', 'password1', '440305199001010001', '张医生', 45, 'M', '广东省深圳市南山区', '13812345678', '深圳市人民医院', '内科', '主任医师', '内科专长'),
    ('doctor2', 'password2', '440305199002020002', '李医生', 38, 'F', '广东省广州市天河区', '13987654321', '广州市中山医院', '外科', '副主任医师', '外科专长'),
    ('doctor3', 'password3', '440305199003030003', '王医生', 52, 'M', '湖南省长沙市岳麓区', '18012345678', '长沙市第一人民医院', '儿科', '主任医师', '儿科专长'),
    ('doctor4', 'password4', '440305199004040004', '赵医生', 41, 'F', '江苏省南京市建邺区', '15912345678', '南京市鼓楼医院', '妇产科', '副主任医师', '妇产科专长'),
    ('doctor5', 'password5', '440305199005050005', '孙医生', 35, 'M', '浙江省杭州市西湖区', '13712345678', '杭州市第一人民医院', '神经科', '主治医师', '神经科专长'),
    ('doctor6', 'password6', '440305199006060006', '周医生', 47, 'F', '福建省福州市鼓楼区', '18912345678', '福州市第二人民医院', '眼科', '副主任医师', '眼科专长'),
    ('doctor7', 'password7', '440305199007070007', '吴医生', 39, 'M', '四川省成都市武侯区', '15012345678', '成都市第三人民医院', '皮肤科', '主治医师', '皮肤科专长'),
    ('doctor8', 'password8', '440305199008080008', '郑医生', 43, 'F', '广西壮族自治区南宁市青秀区', '13612345678', '南宁市第一人民医院', '骨科', '主任医师', '骨科专长'),
    ('doctor9', 'password9', '440305199009090009', '马医生', 49, 'M', '河北省石家庄市长安区', '18712345678', '石家庄市中心医院', '心内科', '副主任医师', '心内科专长'),
    ('doctor10', 'password10', '440305199010100010', '刘医生', 42, 'F', '山东省青岛市市南区', '15512345678', '青岛市市立医院', '普外科', '主治医师', '普外科专长');

INSERT INTO t_admin (user_name, password, id_number, name, address, contact) VALUES
    ('admin', 'password', '440305199001010001', '张明', '广东省深圳市南山区', '13812345678'),
    ('admin1', 'password1', '440305199001010011', '宋浩', '广东省深圳市南山区', '13812345678'),
    ('admin2', 'password2', '440305199002020002', '李晓', '广东省广州市天河区', '13987654321'),
    ('admin3', 'password3', '440305199003030003', '王红', '湖南省长沙市岳麓区', '18012345678'),
    ('admin4', 'password4', '440305199004040004', '赵娜', '江苏省南京市建邺区', '15912345678'),
    ('admin5', 'password5', '440305199005050005', '孙强', '浙江省杭州市西湖区', '13712345678'),
    ('admin6', 'password6', '440305199006060006', '周丽', '福建省福州市鼓楼区', '18912345678'),
    ('admin7', 'password7', '440305199007070007', '吴刚', '四川省成都市武侯区', '15012345678'),
    ('admin8', 'password8', '440305199008080008', '郑梅', '广西壮族自治区南宁市青秀区', '13612345678'),
    ('admin9', 'password9', '440305199009090009', '马华', '河北省石家庄市长安区', '18712345678'),
    ('admin10', 'password10', '440305199010100010', '刘晶', '山东省青岛市市南区', '15512345678');






