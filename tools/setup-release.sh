#! /bin/sh
maybe_install_gpg() {
    # Install gnupgV2 if missing
    if ! gpg --version > /dev/null; then
        echo "No gnupg* found. Installing gnupg2..."
        apt install gnupg2
    fi
}

maybe_install_conv() {
    # Install conventional-changelog-cli if missing
    npm list -g conventional-changelog-cli > /dev/null || npm install -g conventional-changelog-cli
}

setup_gradle_signing() {
    # Setup gradle properties for gpg signing artifacts
    GRADLE_FOLDER=~/.gradle/
    GRADLE_PROPERTIES=$GRADLE_FOLDER/gradle.properties
    KEY_PATH=`pwd`/tools/bitrise-secret.key
    KEY_ID=9719DC41

    if [ ! -d $GRADLE_FOLDER ]
    then
        mkdir $GRADLE_FOLDER
    fi

    if [ -e $GRADLE_PROPERTIES ]
    then
        if grep -q "signing." $GRADLE_PROPERTIES
        then
            echo "The gradle properties already have a signing profile, leaving untouched."
            return 0
        fi
    else
        touch $GRADLE_PROPERTIES
    fi

    echo "Adding bitrise key to gradle properties..."
    cat > $GRADLE_PROPERTIES << EOF
signing.keyId=$KEY_ID
signing.password=
signing.secretKeyRingFile=$KEY_PATH
EOF
    echo "New gradle properties:"
    cat $GRADLE_PROPERTIES
    gpg --import $KEY_PATH
    git config user.signingkey $KEY_ID
}

# Maven drop repository handling TODO: Remove if alternative https://issues.sonatype.org/browse/OSSRH-39124
drop_open_repo() {
    echo "Creating minimal pom to use nexus maven plugin"
    cat > pom.xml << EOF
<project>
    <groupId>com.algolia</groupId>
    <artifactId>algoliasearch-android</artifactId>
    <version>0.0.0</version>
    <modelVersion>4.0.0</modelVersion>
    <build>
        <plugins>
            <plugin>
                <groupId>org.sonatype.plugins</groupId>
                <artifactId>nexus-staging-maven-plugin</artifactId>
                <version>1.6.6</version>
                <extensions>true</extensions>
                <configuration>
                    <serverId>nexus</serverId>
                    <nexusUrl>https://oss.sonatype.org/</nexusUrl>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
EOF

    echo "Creating local maven settings"
    cat > mvn-settings.xml << EOF
<settings>
    <servers>
        <server>
            <id>nexus</id>
            <username>$NEXUS_USERNAME</username>
            <password>$NEXUS_PASSWORD</password>
        </server>
    </servers>
</settings>
EOF

    MVN_REPO_ID=firsttry
    while test -z $MVN_REPO_ID #TODO: Avoid do while by getting every ID from the next call
        MVN_REPO_ID=`mvn --settings mvn-settings.xml nexus-staging:rc-list | grep comalgolia | cut -d' ' -f 2`
        if test -z $MVN_REPO_ID; then
            echo "No repository was currently open."
            break;
        else
            echo "Existing repository to close: $MVN_REPO_ID"
            mvn --settings=mvn-settings.xml nexus-staging:drop -DstagingRepositoryId=$MVN_REPO_ID
        fi
    do
        :
    done
    echo "Removing maven-related files..."
    rm pom.xml
    rm mvn-settings.xml
}

maybe_install_gpg
maybe_install_conv
drop_open_repo
setup_gradle_signing
