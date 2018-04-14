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

### Gmail support

Gmail allows users to create email aliases by inserting `+` and `.` characters
in the local part of their email address.

For example, emails sent to `foo.bar+baz@gmail.com` and `foobar@gmail.com` end
up at the same place.

https://gmail.googleblog.com/2008/03/2-hidden-ways-to-get-more-from-your.html

Unfortunately, this breaks our ability to lookup Gravatar profiles as the extra
characters generate new hashes that don't line up with the base email.

To make matters more complicated, gmail allows custom domains, so we have no
certain way of knowing whether any given address implements this aliasing logic.

For higher level functions, such as the Hoplon elements, we first poll Gravatar
for a profile against the raw email address provided then attempt to fallback to
a "normalized" gmail alias version of the email address.

`hoplon-gravatar.gmail/maybe-alias?` returns true if the passed email address
contains a character in the local component that might indicate an alias.

`hoplon-gravatar.gmail/normalize-email-alias` returns a normalized version of an
email address without any alias characters in it.
