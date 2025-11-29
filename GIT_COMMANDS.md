# Git Commands for TokenAuth Project

## Initial Setup (if repository doesn't exist)

```bash
# Initialize Git repository
git init

# Add all files
git add .

# Make first commit
git commit -m "Initial commit: TokenAuth mod for Minecraft 1.21.5"
```

## Connect to GitHub Repository

```bash
# Add remote repository (replace YOUR_USERNAME with your GitHub username)
git remote add origin https://github.com/YOUR_USERNAME/tokenauth.git

# Or if using SSH
git remote add origin git@github.com:Jenazzz/TokenAuth.git
```

## Commit and Push

```bash
# Check status
git status

# Add all changes
git add .

# Commit changes
git commit -m "Your commit message here"

# Push to GitHub (first time)
git push -u origin main

# Or if your default branch is master
git push -u origin master

# Subsequent pushes
git push
```

## Common Commands

```bash
# See what files are changed
git status

# See differences
git diff

# View commit history
git log

# Create a new branch
git checkout -b feature-name

# Switch branches
git checkout branch-name
```

## If Git is not in PATH

1. **Restart your terminal/IDE** after installing Git
2. **Or use full path**: `C:\Program Files\Git\bin\git.exe` instead of `git`
3. **Or add Git to PATH**:
   - Open System Properties → Environment Variables
   - Add `C:\Program Files\Git\bin` to PATH

## Using IntelliJ IDEA

1. **VCS → Git → Add** (to add files)
2. **VCS → Commit** (Ctrl+K) to commit
3. **VCS → Git → Push** (Ctrl+Shift+K) to push
4. **VCS → Git → Remotes** to add remote repository



