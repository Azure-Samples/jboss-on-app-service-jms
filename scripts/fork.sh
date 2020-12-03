SCRIPTDIRPATH="$(cd -P -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd -P)"

mkdir -p "$SCRIPTDIRPATH/.scripts"
cp $SCRIPTDIRPATH/scripts/* $SCRIPTDIRPATH/.scripts/
