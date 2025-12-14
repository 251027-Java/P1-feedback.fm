# Maven Setup Guide

## Issue: "mvn command not found"

If you see this error, it means Maven is either:
1. Not installed on your system
2. Not added to your PATH environment variable

## Solution: Use Maven Wrapper (Recommended)

**Good news!** The project includes a Maven Wrapper (`mvnw` or `mvnw.cmd`) that doesn't require Maven installation.

### On Windows (PowerShell or Command Prompt):

```bash
cd backend
.\mvnw.cmd clean install
.\mvnw.cmd spring-boot:run
```

### On Windows (Git Bash or WSL):

```bash
cd backend
./mvnw clean install
./mvnw spring-boot:run
```

### On Mac/Linux:

```bash
cd backend
./mvnw clean install
./mvnw spring-boot:run
```

---

## Alternative: Install Maven

If you prefer to install Maven globally:

### Windows Installation:

1. **Download Maven:**
   - Visit: https://maven.apache.org/download.cgi
   - Download `apache-maven-3.9.x-bin.zip`

2. **Extract the archive:**
   - Extract to `C:\Program Files\Apache\maven` (or any location)

3. **Add to PATH:**
   - Open System Properties → Environment Variables
   - Under "System Variables", find "Path" and click "Edit"
   - Click "New" and add: `C:\Program Files\Apache\maven\bin`
   - Click "OK" on all dialogs

4. **Verify installation:**
   - Open a **new** Command Prompt or PowerShell
   - Run: `mvn --version`
   - You should see Maven version information

### After Installation:

Restart your terminal/IDE, then you can use:
```bash
mvn clean install
mvn spring-boot:run
```

---

## Quick Reference

| Task | Maven Wrapper (No Install) | Maven (After Install) |
|------|---------------------------|----------------------|
| Build | `.\mvnw.cmd clean install` | `mvn clean install` |
| Run | `.\mvnw.cmd spring-boot:run` | `mvn spring-boot:run` |
| Test | `.\mvnw.cmd test` | `mvn test` |
| Coverage | `.\mvnw.cmd clean test jacoco:report` | `mvn clean test jacoco:report` |

---

## Recommendation

**Use Maven Wrapper** (`mvnw.cmd`) - it's included in the project and works immediately without any installation or PATH configuration.
