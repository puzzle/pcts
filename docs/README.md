# Docs

The documentation is generated with the help of [mdBook](https://rust-lang.github.io/mdBook/).
The documentation is written in markdown, then built and deployed
to GitHub Pages. You can access it [here](https://puzzle.github.io/pcts/).

## Development

To change any documentation, simply change the markdown files in `./docs`.

If you want to preview your changes, which you should, you also need to install mdBook.

You can do so with the package manager of your choice:

```shell
sudo apt install mdbook mdbook-plantuml
```

*Note that you also need to install `mdbook-plantuml` to render the diagrams.*

For other installation methods, see [the official documentation](https://rust-lang.github.io/mdBook/guide/installation.html).

## Deploy

To deploy, simply merge your changes into the main branch.
This will trigger [this workflow](./../.github/workflows/documentation.yaml), which
deploys it to GitHub Pages.

## Documentation

You can access the [mdBook documentation here](https://rust-lang.github.io/mdBook),
which does a good job of explaining how this works and what is possible.
