"""Report whether the optional release-signing secrets are configured."""

import os
from pathlib import Path


REQUIRED_SECRETS = ("KEYSTORE", "KEY_ALIAS", "KEYSTORE_PASSWORD", "KEY_PASSWORD")


def missing_signing_secrets(environment):
    return [name for name in REQUIRED_SECRETS if not environment.get(name, "").strip()]


def main():
    missing = missing_signing_secrets(os.environ)
    enabled = not missing
    output = os.environ.get("GITHUB_OUTPUT")
    if output:
        with Path(output).open("a", encoding="utf-8") as handle:
            handle.write(f"enabled={str(enabled).lower()}\n")
    message = (
        "Release signing is configured."
        if enabled
        else "Signed releases are skipped: configure these repository secrets: "
        + ", ".join(missing)
        + ". Debug builds and tests do not require them."
    )
    print(message)
    summary = os.environ.get("GITHUB_STEP_SUMMARY")
    if summary:
        with Path(summary).open("a", encoding="utf-8") as handle:
            handle.write(message + "\n")


if __name__ == "__main__":
    main()
