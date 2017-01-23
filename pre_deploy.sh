#! /bin/sh
function find_replace() {
if [ $# -ne 2 ]; then
    echo "Usage: $0 TERM_TO_FIND REPLACEMENT"
    return -1;
fi
echo "Finding $1 and replacing by $2..."
rg -l "$1" . 2>&1 | xargs -I {} sed -i '' "s@$1@$2@" {} 
}

find_replace "stylesheets/" "https://community.algolia.com/instantsearch-android/stylesheets/"
find_replace "../assets/" "/assets/"
