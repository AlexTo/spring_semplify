import React, {Fragment, useEffect, useState} from "react";
import {
  Chip,
  Tooltip,
  Grid,
  Typography,
  Divider
} from "@material-ui/core";

import {withStyles, makeStyles} from '@material-ui/core/styles';
import {entityQueries} from "../../queries/entityQueries";
import {useLazyQuery} from "@apollo/react-hooks";
import Highlighter from "react-highlight-words";

const HtmlTooltip = withStyles(theme => ({
  tooltip: {
    backgroundColor: 'white',
    color: 'rgba(0, 0, 0, 0.87)',
    maxWidth: 600,
    fontSize: theme.typography.pxToRem(12),
    border: '1px solid #dadde9',
  },
}))(Tooltip);

const useStyles = makeStyles(theme => ({
  root: {
    flexGrow: 1
  },
  img: {
    margin: 'auto',
    display: 'block',
    maxWidth: '100%',
  }
}));

function EntityChip({className, size, uri, prefLabel, surfaceForm, context, color, onClick, onDelete}) {
  const classes = useStyles();
  const [open, setOpen] = useState(false);

  const [loadThumbnail, {called: calledLoadThumbnail, loading: loadingThumbnail, data: dataThumbnail}] = useLazyQuery(entityQueries.thumbnail,
    {
      variables: {uri: uri}
    });

  useEffect(() => {
    if (!open)
      return;
    loadThumbnail();
  }, [open]);

  const handleClose = () => {
    setOpen(false);
  };

  const handleOpen = () => {
    setOpen(true);
  };


  return (
    <HtmlTooltip
      title={
        <div className={classes.root}>
          <Grid container spacing={2}>
            {(dataThumbnail && dataThumbnail.thumbnail
              && dataThumbnail.thumbnail.thumbnailUri) && <Grid item md={4}>
              <img
                className={classes.img}
                src={dataThumbnail.thumbnail.thumbnailUri}
                alt={prefLabel}
              />
            </Grid>}
            <Grid item md>
              <Grid container>
                <Grid item md={12}>
                  <Typography>{prefLabel}</Typography>
                </Grid>
                <Grid item md={12}>
                  <Typography color="secondary">{uri}</Typography>
                </Grid>
                {context && <Fragment>
                  <hr/>
                  <Grid item md={12}>
                    <Highlighter
                      searchWords={[surfaceForm]}
                      textToHighlight={context}
                    /></Grid>
                </Fragment>}
              </Grid>
            </Grid>
          </Grid>
        </div>
      }
      open={open} onClose={handleClose} onOpen={handleOpen} placement="bottom">
      <Chip
        className={className}
        size={size}
        label={prefLabel}
        color={color}
        onDelete={onDelete}
        onClick={onClick}/>
    </HtmlTooltip>
  )
}

export default EntityChip;
