# 阶段 1：使用 Maven 打包（Java 21）
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
# 先下载依赖，利用 Docker 缓存层加速后续构建
RUN mvn dependency:go-offline -B
COPY src ./src
# 跳过测试以加快在 Render 上的构建速度
RUN mvn clean package -DskipTests -B

# 阶段 2：使用轻量级 JRE 运行
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
# 使用 shell 模式使 ${PORT:-8080} 环境变量替换生效（Render 等平台会动态分配端口）
ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=${PORT:-8080}"]
