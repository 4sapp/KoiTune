import unittest

from check_signing_config import REQUIRED_SECRETS, missing_signing_secrets


class SigningConfigTest(unittest.TestCase):
    def test_requires_every_signing_secret(self):
        configured = dict.fromkeys(REQUIRED_SECRETS, "present")
        self.assertEqual(missing_signing_secrets(configured), [])
        for name in REQUIRED_SECRETS:
            with self.subTest(name=name):
                environment = configured.copy()
                environment[name] = " \n"
                self.assertEqual(missing_signing_secrets(environment), [name])

    def test_fresh_fork_reports_all_missing_secrets(self):
        self.assertEqual(missing_signing_secrets({}), list(REQUIRED_SECRETS))


if __name__ == "__main__":
    unittest.main()
