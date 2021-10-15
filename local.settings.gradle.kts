includeBuild("/Users/mouaad.aallam/Developer/Algolia/client/algoliasearch-client-kotlin") {
    dependencySubstitution {
        substitute(module("com.algolia:algoliasearch-client-kotlin"))
            .with(project(":client"))
    }
}
