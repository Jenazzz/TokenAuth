# Quick Start - Upload to GitHub

## Repository URL
https://github.com/Jenazzz/TokenAuth

## Commands to run (after restarting terminal/IDE):

```bash
# Navigate to project folder
cd "C:\Users\strip\IdeaProjects\untitled"

# Initialize Git (if not done)
git init

# Add remote repository
git remote add origin https://github.com/Jenazzz/TokenAuth.git

# Add all files
git add .

# Commit
git commit -m "Initial commit: TokenAuth mod for Minecraft 1.21.5 - Ported by Rect"

# Set main branch
git branch -M main

# Push to GitHub
git push -u origin main
```

## If Git is still not found:

1. **Restart IntelliJ IDEA completely**
2. **Or find Git path** and use full path:
   ```powershell
   & "C:\Program Files\Git\bin\git.exe" --version
   ```
3. **Or add Git to PATH manually**:
   - Press `Win + R`, type `sysdm.cpl`, press Enter
   - Go to "Advanced" tab → "Environment Variables"
   - Under "System variables", find "Path", click "Edit"
   - Click "New" and add: `C:\Program Files\Git\bin`
   - Click OK on all windows
   - **Restart your computer** or at least restart all terminals/IDEs



