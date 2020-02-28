import React, {Fragment, useEffect} from "react";
import {colors, Grid, Snackbar, SnackbarContent, Typography} from "@material-ui/core";
import {useDispatch, useSelector} from "react-redux";
import {entityActions} from "../../actions";
import {makeStyles} from "@material-ui/styles";
import {useLazyQuery} from "@apollo/react-hooks";
import {entityQueries} from "../../queries/entityQueries";
import Link from "@material-ui/core/Link";

const useStyles = makeStyles((theme) => ({
  root: {
    flexGrow: 1,
    maxWidth: 700
  },
  img: {
    margin: 'auto',
    display: 'block',
    maxWidth: '100%',
  },
  content: {
    backgroundColor: theme.palette.common.white
  }
}));

function EntitySummarySnackbar() {
  const classes = useStyles();
  const entityReducer = useSelector(state => state.entityReducer);
  const dispatch = useDispatch();
  const {popupSummaryOpen, popupSummaryUri} = entityReducer;

  const [loadSummary, {called, loading, data}] = useLazyQuery(entityQueries.summary,
    {
      variables: {uri: popupSummaryUri}
    });

  useEffect(() => {
    if (!popupSummaryOpen)
      return;
    loadSummary();
  }, [popupSummaryOpen]);

  const summary = data ? data.summary : undefined;


  return (
    <div className={classes.root}>
      <Snackbar open={popupSummaryOpen}
                anchorOrigin={{
                  vertical: 'top',
                  horizontal: 'right'
                }}
                onClose={() => dispatch(entityActions.hidePopupSummary())}
                autoHideDuration={5000}>
        <SnackbarContent className={classes.content} message={(called && loading) ? <span></span> :
          <div className={classes.root}>
            <Grid container spacing={2}>
              {(summary && summary.thumbnail
                && summary.thumbnail.thumbnailUri) && <Grid item md={4}>
                <img
                  className={classes.img}
                  src={summary.thumbnail.thumbnailUri}
                  alt="prefLabel"
                />
              </Grid>}
              <Grid item md>
                <Grid container spacing={2}>
                  {summary && summary.prefLabel && summary.prefLabel.prefLabel && <Grid item md={12}>
                    <Typography>{summary.prefLabel.prefLabel}</Typography>
                  </Grid>}
                  {popupSummaryUri && <Grid item md={12}>
                    <Link href={popupSummaryUri}
                          target="_blank"
                          rel="noopener">
                      {popupSummaryUri}
                    </Link>
                  </Grid>}
                  {summary && summary.abstract_ && summary.abstract_.text && <Grid item md={12}>
                    <Typography>{summary.abstract_.text}</Typography>}
                  </Grid>}
                </Grid>
              </Grid>
            </Grid>
          </div>
        }/>
      </Snackbar>
    </div>
  )
}

export default EntitySummarySnackbar;
