set -e
cd "$(dirname "$0")"
chmod +x gradlew
if [ ! -f gradle/wrapper/gradle-wrapper.jar ]; then
  curl -fsSL https://raw.githubusercontent.com/gradle/gradle/v9.8.0/gradle/wrapper/gradle-wrapper.jar -o gradle/wrapper/gradle-wrapper.jar
fi
REPO="$1"
[ -z "$REPO" ] && { printf "GitHub repo URL: "; read REPO; }
[ -d .git ] || git init -b main
git add -A
git -c user.name="croxy" -c user.email="croxy@users.noreply.github.com" commit -m "CroxyWare build" || true
git remote remove origin 2>/dev/null || true
git remote add origin "$REPO"
git push -u origin main --force
echo "Tamam! GitHub > Actions sekmesine bak."
