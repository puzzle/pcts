import os
import sys
import random
from plexapi.server import PlexServer
from plexapi.library import Library
from plexapi.collection import Collection
from typing import List


def get_env_var(name: str) -> str:
    result = os.environ.get(name)
    if result == None:
        sys.exit("Environment variable %s is not defined, exiting script!" % name)
    return result


def get_int(name: str, default: int) -> int:
    value = os.environ.get(name)
    if value != None and value.isdigit():
        return int(value)
    else:
        return default


def get_list(name: str, default: List[str]) -> List[str]:
    value = os.environ.get(name)
    if value == None:
        return default
    return value.split(";")


def get_bool(name: str, default: bool) -> bool:
    value = os.environ.get(name)
    if value != None:
        return value in value in ["true", "1", "yes"]
    else:
        return default


def get_libraries(server: PlexServer, library_types: List[str]) -> List[Library]:
    return [
        section
        for section in server.library.sections()
        if section.type in library_types
    ]


def get_all_collections(libraries: List[Library]) -> List[Collection]:
    all_collections = []
    for library in libraries:
        all_collections.extend(library.collections())
    return all_collections


def is_pinned(collection: Collection) -> bool:
    return (
        collection.visibility().promotedToOwnHome
        or collection.visibility().promotedToRecommended
        or collection.visibility().promotedToSharedHome
    )


def unpin_from_everywhere(collection: Collection) -> None:
    hub = collection.visibility()
    hub.demoteRecommended()
    hub.demoteHome()
    hub.demoteShared()


def pin_collection(collection: Collection) -> None:
    hub = collection.visibility()
    hub.promoteRecommended()
    hub.promoteHome()
    hub.promoteShared()


def collection_is_duplicate(
    collections: List[Collection],
    collection_in_question: Collection,
    allow_duplicates: bool,
):
    if allow_duplicates:
        return False

    titles = [c.title for c in collections]
    is_duplicate = collection_in_question.title in titles
    if is_duplicate:
        print(
            f"Collection {collection_in_question.title} will not be pinned, because there is already a collection with the same title pinned"
        )
    return is_duplicate


def pin_random_collections(
    collections: List[Collection], amount: int, min: int, allow_duplicates: bool
) -> None:
    pinned_collections = []

    for _ in range(amount):
        collection_to_pin = random.choice(collections)
        while (
            collection_to_pin in pinned_collections
            or collection_to_pin.childCount is None
            or collection_to_pin.childCount < min
            or collection_is_duplicate(
                pinned_collections, collection_to_pin, allow_duplicates
            )
        ):
            collection_to_pin = random.choice(collections)

        print(f"    - Pinning {collection_to_pin.title}")
        pin_collection(collection_to_pin)


def main():
    PLEX_BASE_URL = get_env_var("PPCR_BASE_URL")
    PLEX_TOKEN = get_env_var("PPCR_TOKEN")
    INCLUDED_LIBRARY_TYPES = get_list("PPCR_INCLUDED_LIBRARY_TYPES", ["movie", "show"])
    AMOUNT = get_int("PPCR_AMOUNT", 5)
    MIN = get_int("PPCR_MIN_AMOUNT_IN_COLLECTION", 0)
    ALLOW_DUPLICATES = get_bool("PPCR_ALLOW_DUPLICATES", True)
    ALWAYS_PIN = get_list("PPCR_ALWAYS_PIN", [])
    VERSION = "1.0.0"  # x-release-please-version

    print(f"Version: {VERSION}")
    print("Starting with the following configuration:")
    print(f"    - Plex URL: {PLEX_BASE_URL}")
    print(f"    - Plex Token: {'*****' if len(PLEX_TOKEN) > 0 else 'EMPTY!'}")
    print(f"    - Amount of collections to pin: {AMOUNT}")
    print(f"    - Minimum of movies in collection to allow for pinning: {MIN}")
    print(f"    - Included types of libraries: {INCLUDED_LIBRARY_TYPES}")
    print(
        f"    - Pinning collections with the same name is: {'ENABLED' if ALLOW_DUPLICATES else 'DISABLED'  }"
    )
    print(f"    - Found {len(ALWAYS_PIN)} collections to always pin:")
    for collection_to_pin in ALWAYS_PIN:
        print(f"        - {collection_to_pin}")

    plex = PlexServer(PLEX_BASE_URL, PLEX_TOKEN)

    libraries = get_libraries(plex, INCLUDED_LIBRARY_TYPES)
    print("Detected the following libraries:")
    for library in libraries:
        print(f"    - {library.title} ({library.type})")

    print("Detecting all available collections:")
    all_collections = get_all_collections(libraries)
    all_collections_length = len(all_collections)
    print(f"    - Detected {all_collections_length} collections in total")
    if all_collections_length < AMOUNT:
        printf(
            f"The amount of all available collections ({all_collections_length}) is lower than the amount of requested pinned collections ({AMOUNT}), lowering the amount to {all_collections_length}"
        )
        AMOUNT = all_collections_length

    collections_to_always_pin = []
    print("Upinning all collections:")
    for collection in all_collections:
        if any(keyword in collection.title for keyword in ALWAYS_PIN):
            print(
                f"    - NOT unpinning {collection.title} because it is marked as always pinned"
            )
            collections_to_always_pin.append(collection)
        else:
            if is_pinned(collection):
                print(f"    - Unpinning {collection.title}")
                unpin_from_everywhere(collection)
            else:
                print(
                    f"    - NOT unpinning {collection.title} because it is not already pinned anywhere anyway"
                )

    print(f"Pinning {AMOUNT} random collections")
    pin_random_collections(all_collections, AMOUNT, MIN, ALLOW_DUPLICATES)

    print("Additionally pinning the following collections: ")
    for collection in collections_to_always_pin:
        print(f"    - {collection.title}")
        pin_collection(collection)


if __name__ == "__main__":
    main()