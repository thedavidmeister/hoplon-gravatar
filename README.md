# hoplon-gravatar

"Batteries included" Gravatar integration for Hoplon.

## Installation

## Usage

There are multiple namespaces in `hoplon-gravatar` moving from low level config
and API wrappers up to Hoplon elements for profile pictures and usernames that
Just Works.

### Low level API

The `hoplon-gravatar.api` namespace contains low level fns for normalization of
emails and hashing to match Gravatar's API requirements.

## Gmail support

Gmail allows users to create email aliases by inserting `+` and `.` characters
in the local part of their email address.

For example, emails sent to `foo.bar+baz@gmail.com` and `foobar@gmail.com` end
up at the same place.

Unfortunately, this breaks our ability to lookup Gravatar profiles as the extra
characters generate new hashes that don't line up with the base email.

`hoplon-gravatar.api/should-normalize-email?`
